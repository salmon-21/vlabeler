package com.sdercolin.vlabeler.model

import androidx.compose.runtime.Immutable
import com.sdercolin.vlabeler.env.Log
import com.sdercolin.vlabeler.io.WaveLoadingAlgorithmVersion
import com.sdercolin.vlabeler.io.getSampleValueFromFrame
import com.sdercolin.vlabeler.io.normalize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.serialization.Serializable
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.pow

/**
 * Information of a sample file read at the first time and stored for later use.
 *
 * @property name The name with extension of the sample file.
 * @property file The path of the sample file relative to the project root directory.
 * @property sampleRate The sample rate of the sample file.
 * @property maxSampleRate The maximum sample rate according to the current configuration.
 * @property normalize Whether to normalize the sample file.
 * @property normalizeRatio The ratio of the normalization.
 * @property channels The number of channels in the sample file.
 * @property length The number of frames in the sample file.
 * @property lengthMillis The length of the sample file in milliseconds.
 * @property chunkSize The number of frames in each chunk. The last chunk may have fewer frames, but it doesn't matter
 *    because the chunkSize is used for calculating the offset of the current chunk.
 * @property chunkCount The number of chunks.
 * @property hasSpectrogram Whether spectrogram is loaded for the sample file.
 * @property lastModified The last modified time of the sample file.
 * @property algorithmVersion The version of the algorithm used to load the sample file.
 */
@Serializable
@Immutable
data class SampleInfo(
    val name: String,
    val file: String,
    val sampleRate: Float,
    val maxSampleRate: Int,
    val normalize: Boolean,
    val normalizeRatio: Float?,
    val channels: Int,
    val length: Int,
    val lengthMillis: Float,
    val chunkSize: Int,
    val chunkCount: Int,
    val hasSpectrogram: Boolean,
    val lastModified: Long,
    val algorithmVersion: Int,
) {

    fun getFile(project: Project): File = project.rootSampleDirectory.resolve(file)

    companion object {

        suspend fun load(project: Project, file: File, appConf: AppConf): Result<SampleInfo> = runCatching {
            val stream = AudioSystem.getAudioInputStream(file)
            val maxSampleRate = appConf.painter.amplitude.resampleDownToHz
            val format = stream.format.normalize(maxSampleRate)
            Log.debug("Sample info loaded: $format")
            val channelNumber = format.channels
            val frameLengthLong = stream.frameLength * format.sampleRate / stream.format.sampleRate
            if (frameLengthLong > Int.MAX_VALUE) {
                throw IllegalArgumentException(
                    "Cannot load sample with frame length ($frameLengthLong) > ${Int.MAX_VALUE}",
                )
            }
            val frameLength = frameLengthLong.toInt()
            val channels = (0 until channelNumber).map { mutableListOf<Float>() }
            if (stream.format.encoding !in arrayOf(AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_FLOAT)) {
                throw Exception("Unsupported audio encoding: ${format.encoding}")
            }
            val maxChunkSize = appConf.painter.maxDataChunkSize
            val sampleRate = format.sampleRate
            val lengthInMillis = frameLength / sampleRate * 1000
            val chunkCount = ceil(frameLength.toDouble() / maxChunkSize).toInt()
            val chunkSize = frameLength / chunkCount

            var normalizeRatio: Float? = null
            val normalize = appConf.painter.amplitude.normalize
            if (normalize) {
                val convertedStream = AudioSystem.getAudioInputStream(format, stream)
                val peakValue = loadPeakValue(convertedStream)
                val maxValue = 2.0.pow(format.sampleSizeInBits - 1).toFloat()
                if (peakValue > 0) {
                    normalizeRatio = maxValue / peakValue
                }
                convertedStream.close()
            }
            stream.close()

            val filePath = file.relativeTo(project.rootSampleDirectory).path.replace(File.separatorChar, '/')

            SampleInfo(
                name = file.name,
                file = filePath,
                sampleRate = sampleRate,
                maxSampleRate = maxSampleRate,
                normalize = normalize,
                normalizeRatio = normalizeRatio,
                channels = channels.size,
                length = frameLength,
                lengthMillis = lengthInMillis,
                chunkSize = chunkSize,
                chunkCount = chunkCount,
                hasSpectrogram = appConf.painter.spectrogram.enabled,
                lastModified = file.lastModified(),
                algorithmVersion = WaveLoadingAlgorithmVersion,
            )
        }

        private suspend fun loadPeakValue(stream: AudioInputStream): Float {
            return withContext(Dispatchers.IO) {
                val format = stream.format
                val sampleByteSize = format.sampleSizeInBits / 8
                val channelCount = format.channels
                val frameSize = sampleByteSize * channelCount
                val isBigEndian = format.isBigEndian
                var pos = 0
                val buffer = ByteArray(frameSize)
                var maxAbsolute = 0f
                while (true) {
                    yield()
                    val readSize = stream.readNBytes(buffer, 0, frameSize)
                    if (readSize == 0) break
                    for (channelIndex in 0 until format.channels) {
                        val sample = getSampleValueFromFrame(buffer, frameSize, channelIndex, channelCount, isBigEndian)
                        maxAbsolute = max(maxAbsolute, sample.absoluteValue)
                    }
                    pos += frameSize
                }
                maxAbsolute
            }
        }
    }
}
