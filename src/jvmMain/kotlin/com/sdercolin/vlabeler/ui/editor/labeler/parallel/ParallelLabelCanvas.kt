package com.sdercolin.vlabeler.ui.editor.labeler

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sdercolin.vlabeler.model.AppConf
import com.sdercolin.vlabeler.model.Module
import com.sdercolin.vlabeler.model.Project
import com.sdercolin.vlabeler.model.SampleInfo
import com.sdercolin.vlabeler.ui.editor.EditorState
import com.sdercolin.vlabeler.ui.editor.IndexedEntry
import com.sdercolin.vlabeler.ui.editor.labeler.marker.EntryConverter
import com.sdercolin.vlabeler.ui.theme.Black
import com.sdercolin.vlabeler.ui.theme.LightGray
import com.sdercolin.vlabeler.util.getScreenRange
import com.sdercolin.vlabeler.util.toRgbColor
import com.sdercolin.vlabeler.util.toRgbColorOrNull

@Composable
fun ParallelLabelCanvas(
    project: Project,
    editorState: EditorState,
    horizontalScrollState: ScrollState,
    canvasParams: CanvasParams,
    sampleInfo: SampleInfo,
    editorConf: AppConf.Editor,
) {
    val modules = remember(project) {
        project.modules.filter { it == project.currentModule || it.isParallelTo(project.currentModule) }
    }

    Column(modifier = Modifier.fillMaxWidth().background(color = Black)) {
        modules.forEachIndexed { index, module ->
            ModuleRow(
                module = module,
                isCurrent = project.currentModule == module,
                editorState = editorState,
                horizontalScrollState = horizontalScrollState,
                canvasParams = canvasParams,
                sampleInfo = sampleInfo,
                editorConf = editorConf,
            )
            val dividerAlpha = if (index == modules.lastIndex) 0.1f else 0.4f
            Divider(color = LightGray.copy(alpha = dividerAlpha), thickness = 1.dp)
        }
    }
}

@Composable
fun ColumnScope.ModuleRow(
    module: Module,
    isCurrent: Boolean,
    editorState: EditorState,
    horizontalScrollState: ScrollState,
    editorConf: AppConf.Editor,
    canvasParams: CanvasParams,
    sampleInfo: SampleInfo,
) {
    val screenRange = horizontalScrollState.getScreenRange(canvasParams.lengthInPixel)
    val entryConverter = remember(sampleInfo.sampleRate, canvasParams.resolution) {
        EntryConverter(sampleInfo.sampleRate, canvasParams.resolution)
    }
    val entriesInPixel = remember(module.currentEntryGroup, canvasParams.lengthInPixel, sampleInfo.lengthMillis) {
        module.currentEntryGroup.map { entry: IndexedEntry ->
            entryConverter.convertToPixel(entry, sampleInfo.lengthMillis)
                .validate(canvasParams.lengthInPixel)
        }
    }
    val visibleEntries = remember(entriesInPixel, screenRange) {
        if (screenRange == null) {
            emptyList()
        } else {
            entriesInPixel.filterNot { it.end <= screenRange.start || it.start >= screenRange.endInclusive }
        }
    }

    val labelColor = remember(editorConf) {
        editorConf.continuousLabelNames.color.toRgbColorOrNull()
            ?: AppConf.ContinuousLabelNames.DefaultColor.toRgbColor()
    }

    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
        if (screenRange != null) {
            Row(modifier = Modifier.fillMaxSize()) {
                @Composable
                fun RowScope.paddingBox(weight: Float) {
                    if (weight > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(weight),
                        )
                    }
                }

                val paddingStartWeight = (visibleEntries.firstOrNull()?.start?.minus(screenRange.start) ?: 0f) /
                    (screenRange.endInclusive - screenRange.start)
                paddingBox(paddingStartWeight)

                visibleEntries.forEach { entry ->
                    val weight = remember(entry, screenRange) {
                        (
                            entry.end.coerceAtMost(screenRange.endInclusive) -
                                entry.start.coerceAtLeast(screenRange.start)
                            ) /
                            (screenRange.endInclusive - screenRange.start)
                    }
                    if (weight > 0) {
                        Box(
                            modifier = Modifier.fillMaxHeight().weight(weight)
                                .clickable { editorState.jumpToModule(module.name, targetEntryIndex = entry.index) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 2.dp, horizontal = 5.dp),
                                text = entry.name,
                                style = MaterialTheme.typography.caption,
                                color = labelColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                val paddingEndWeight = (visibleEntries.lastOrNull()?.end?.minus(screenRange.endInclusive) ?: 0f) /
                    -(screenRange.endInclusive - screenRange.start)
                paddingBox(paddingEndWeight)
            }
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    val borders = (
                        visibleEntries.map { it.start - screenRange.start } +
                            visibleEntries.lastOrNull()?.end?.let { it - screenRange.start }
                        ).filterNotNull()
                    borders.forEach { border ->
                        drawLine(
                            color = LightGray.copy(alpha = 0.8f),
                            start = Offset(border, 0f),
                            end = Offset(border, size.height),
                            strokeWidth = 2f,
                        )
                    }
                },
            )
            val titleBackgroundColor = if (isCurrent) {
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.surface
            }
            Box(
                modifier = Modifier.fillMaxHeight()
                    .background(titleBackgroundColor)
                    .clickable { editorState.jumpToModule(module.name) }
                    .padding(vertical = 3.dp, horizontal = 7.dp)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
