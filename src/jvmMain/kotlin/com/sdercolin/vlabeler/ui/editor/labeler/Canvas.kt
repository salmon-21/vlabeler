package com.sdercolin.vlabeler.ui.editor.labeler

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.sdercolin.vlabeler.audio.PlayerState
import com.sdercolin.vlabeler.debug.DebugState
import com.sdercolin.vlabeler.env.Log
import com.sdercolin.vlabeler.model.AppConf
import com.sdercolin.vlabeler.model.SampleInfo
import com.sdercolin.vlabeler.repository.ChartRepository
import com.sdercolin.vlabeler.ui.AppState
import com.sdercolin.vlabeler.ui.common.AsyncImage
import com.sdercolin.vlabeler.ui.editor.ChartStore
import com.sdercolin.vlabeler.ui.editor.EditorState
import com.sdercolin.vlabeler.ui.editor.labeler.marker.MarkerCanvas
import com.sdercolin.vlabeler.ui.editor.labeler.marker.MarkerLabels
import com.sdercolin.vlabeler.ui.editor.labeler.marker.MarkerPointEventContainer
import com.sdercolin.vlabeler.ui.editor.labeler.marker.rememberMarkerState
import com.sdercolin.vlabeler.ui.string.Strings
import com.sdercolin.vlabeler.ui.string.string
import com.sdercolin.vlabeler.ui.theme.DarkYellow
import com.sdercolin.vlabeler.util.getScreenRange
import com.sdercolin.vlabeler.util.runIf
import com.sdercolin.vlabeler.util.toColor
import com.sdercolin.vlabeler.util.toColorOrNull
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan

@Composable
fun Canvas(
    horizontalScrollState: ScrollState,
    editorState: EditorState,
    appState: AppState,
) {
    val currentDensity = LocalDensity.current
    val sampleInfoResult = editorState.sampleInfoResult
    val resolution = editorState.canvasResolution

    if (sampleInfoResult != null) {
        val sampleInfo = sampleInfoResult.getOrNull()
        if (sampleInfo != null) {
            val chunkCount = sampleInfo.chunkCount
            val density = LocalDensity.current
            val layoutDirection = LocalLayoutDirection.current
            LaunchedEffect(sampleInfo, appState.appConf, appState.isShowingPrerenderDialog) {
                if (appState.isShowingPrerenderDialog.not()) {
                    editorState.renderCharts(this, sampleInfo, appState.appConf, density, layoutDirection)
                }
            }
            val canvasParams = CanvasParams(sampleInfo.length, sampleInfo.chunkCount, resolution, currentDensity)
            editorState.scrollOnResolutionChangeViewModel.updateCanvasParams(canvasParams, sampleInfo)
            val markerState = rememberMarkerState(sampleInfo, canvasParams, editorState, appState)
            val keyboardState by appState.keyboardViewModel.keyboardStateFlow.collectAsState()
            val screenRange = horizontalScrollState.getScreenRange(markerState.canvasParams.lengthInPixel)

            Column(modifier = Modifier.fillMaxSize()) {
                val project = editorState.project
                val parallelModulesCount = remember(project) {
                    project.modules.count { it.isParallelTo(project.currentModule) && project.labelerConf.continuous }
                }
                if (parallelModulesCount > 0) {
                    Box(modifier = Modifier.weight(0.1f * parallelModulesCount)) {
                        ParallelLabelCanvas(
                            project,
                            editorState,
                            horizontalScrollState,
                            canvasParams,
                            sampleInfo,
                            appState.appConf.editor,
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                    MarkerPointEventContainer(
                        screenRange,
                        keyboardState,
                        horizontalScrollState,
                        markerState,
                        editorState,
                        appState,
                    ) {
                        val lazyListState = rememberLazyListState()
                        LaunchedEffect(Unit) {
                            snapshotFlow { horizontalScrollState.value to horizontalScrollState.maxValue }
                                .scan(null as Pair<Int, Int>? to (0 to 0)) { accumulator, value ->
                                    accumulator.second to value
                                }
                                .onEach { (oldPair, newPair) ->
                                    val (oldValue, oldMax) = oldPair ?: return@onEach
                                    val (newValue, newMax) = newPair
                                    if (oldMax != newMax) {
                                        val itemSize = lazyListState.layoutInfo.visibleItemsInfo.first().size
                                        val itemIndex = newValue / itemSize
                                        val itemOffset = newValue % itemSize
                                        lazyListState.scrollToItem(itemIndex, itemOffset)
                                    } else if (newValue != oldValue) {
                                        lazyListState.scrollBy((newValue - oldValue).toFloat())
                                    }
                                }
                                .launchIn(appState.mainScope)
                        }
                        LazyRow(modifier = Modifier.fillMaxSize(), state = lazyListState) {
                            items(chunkCount) { chunkIndex ->
                                Chunk(
                                    chunkIndex,
                                    canvasParams,
                                    sampleInfo,
                                    appState,
                                    editorState,
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .runIf(appState.isMarkerDisplayed.not()) { alpha(0f) },
                        ) {
                            MarkerCanvas(
                                canvasParams,
                                horizontalScrollState,
                                markerState,
                                editorState,
                                appState,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .horizontalScroll(horizontalScrollState)
                                .runIf(appState.isMarkerDisplayed.not()) { alpha(0f) },
                        ) {
                            MarkerLabels(screenRange, appState, markerState)
                        }
                    }
                    if (appState.playerState.isPlaying) {
                        PlayerCursor(
                            canvasParams,
                            appState.playerState,
                            horizontalScrollState,
                            appState.appConf.editor.playerCursorColor.toColor(),
                        )
                    }
                }
            }
        } else {
            val exception = sampleInfoResult.exceptionOrNull()
            if (exception != null && exception !is CancellationException) {
                Error(string(Strings.FailedToLoadSampleFileError))
            }
        }
    }
}

@Composable
private fun Chunk(
    chunkIndex: Int,
    canvasParams: CanvasParams,
    sampleInfo: SampleInfo,
    appState: AppState,
    editorState: EditorState,
) {
    Box(
        Modifier.fillMaxHeight()
            .requiredWidth(canvasParams.getChunkWidthInDp(chunkIndex))
            .runIf(DebugState.isShowingChunkBorder) { border(1.dp, DarkYellow) },
    ) {
        Column(Modifier.fillMaxSize()) {
            val weightOfEachChannel = 1f / sampleInfo.channels
            val backgroundColor = appState.appConf.painter.amplitude.backgroundColor.toColorOrNull()
                ?: AppConf.Amplitude.DefaultBackgroundColor.toColor()
            repeat(sampleInfo.channels) { channelIndex ->
                Box(Modifier.weight(weightOfEachChannel).fillMaxWidth()) {
                    val imageStatus = editorState.chartStore.getWaveformStatus(channelIndex, chunkIndex)
                    if (imageStatus == ChartStore.ChartLoadingStatus.Loaded) {
                        WaveformChunk(sampleInfo, channelIndex, chunkIndex, backgroundColor)
                    }
                }
            }
            if (sampleInfo.hasSpectrogram && appState.appConf.painter.spectrogram.enabled) {
                Box(
                    Modifier.weight(appState.appConf.painter.spectrogram.heightWeight)
                        .fillMaxWidth(),
                ) {
                    val imageStatus = editorState.chartStore.getSpectrogramStatus(chunkIndex)
                    if (imageStatus == ChartStore.ChartLoadingStatus.Loaded) {
                        SpectrogramChunk(sampleInfo, chunkIndex)
                    }
                }
            }
        }
    }
}

@Composable
private fun WaveformChunk(sampleInfo: SampleInfo, channelIndex: Int, chunkIndex: Int, backgroundColor: Color) {
    Box(Modifier.fillMaxSize().background(backgroundColor)) {
        ChunkAsyncImage(
            load = { ChartRepository.getWaveform(sampleInfo, channelIndex, chunkIndex) },
            sampleInfo,
            channelIndex,
            chunkIndex,
        )
    }
}

@Composable
private fun SpectrogramChunk(sampleInfo: SampleInfo, chunkIndex: Int) {
    Log.info("Spectrogram (chunk $chunkIndex): composed")
    Box(Modifier.fillMaxSize()) {
        ChunkAsyncImage(
            load = { ChartRepository.getSpectrogram(sampleInfo, chunkIndex) },
            sampleInfo,
            chunkIndex,
        )
    }
}

@Composable
private fun ChunkAsyncImage(load: suspend () -> ImageBitmap, vararg keys: Any) {
    AsyncImage(
        load = load,
        painterFor = { remember { BitmapPainter(it) } },
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        keys = keys,
    )
}

@Composable
private fun PlayerCursor(
    canvasParams: CanvasParams,
    playerState: PlayerState,
    scrollState: ScrollState,
    color: Color,
) {
    val screenRange = scrollState.getScreenRange(canvasParams.lengthInPixel)
    Canvas(Modifier.fillMaxSize()) {
        val framePosition = playerState.framePosition
        if (framePosition != null) {
            val actualPosition = framePosition / canvasParams.resolution
            if (screenRange != null && actualPosition in screenRange) {
                val position = actualPosition - screenRange.start
                drawLine(
                    color = color,
                    start = Offset(position, 0f),
                    end = Offset(position, center.y * 2),
                    strokeWidth = 2f,
                )
            }
        }
    }
}

@Composable
private fun Error(text: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.error.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.widthIn(max = 600.dp),
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
        )
    }
}
