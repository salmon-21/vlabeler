package com.sdercolin.vlabeler.ui.dialog

import androidx.compose.runtime.Composable
import com.sdercolin.vlabeler.env.Log
import com.sdercolin.vlabeler.io.exportProjectModule
import com.sdercolin.vlabeler.io.importProjectFile
import com.sdercolin.vlabeler.io.loadProject
import com.sdercolin.vlabeler.model.Project
import com.sdercolin.vlabeler.ui.AppState
import com.sdercolin.vlabeler.ui.string.Strings
import com.sdercolin.vlabeler.ui.string.string
import com.sdercolin.vlabeler.util.getDirectory
import com.sdercolin.vlabeler.util.lastPathSection
import com.sdercolin.vlabeler.util.moveCacheDirTo
import com.sdercolin.vlabeler.util.toFile
import com.sdercolin.vlabeler.util.toFileOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun StandaloneDialogs(
    mainScope: CoroutineScope,
    appState: AppState,
) {
    when {
        appState.isShowingOpenProjectDialog -> OpenFileDialog(
            title = string(Strings.OpenProjectDialogTitle),
            extensions = listOf(Project.ProjectFileExtension),
        ) { parent, name ->
            appState.closeOpenProjectDialog()
            if (parent != null && name != null) {
                loadProject(mainScope, File(parent, name), appState)
            }
        }
        appState.isShowingSaveAsProjectDialog -> SaveFileDialog(
            title = string(Strings.SaveAsProjectDialogTitle),
            extensions = listOf(Project.ProjectFileExtension),
            initialDirectory = appState.requireProject().workingDirectory.absolutePath,
            initialFileName = appState.requireProject().projectFile.name,
        ) { directory, fileName ->
            appState.closeSaveAsProjectDialog()
            if (directory != null && fileName != null) {
                appState.editProject {
                    val projectName = File(fileName).nameWithoutExtension
                    val cacheDirectory = if (isUsingDefaultCacheDirectory) {
                        val newCacheDirectory = Project.getDefaultCacheDirectory(directory, projectName)
                        runCatching {
                            moveCacheDirTo(newCacheDirectory.toFile(), clearOld = false)
                        }.onFailure {
                            Log.error(it)
                        }
                        newCacheDirectory
                    } else this.cacheDirectoryPath
                    copy(
                        workingDirectoryPath = directory,
                        projectName = projectName,
                        cacheDirectoryPath = cacheDirectory,
                    )
                        .makeRelativePathsIfPossible()
                        .also {
                            appState.addRecentProject(it.projectFile)
                        }
                }
                appState.requestSave()
            }
        }
        appState.isShowingExportDialog -> {
            val project = appState.requireProject()
            val currentModuleNameSection = project.currentModule.name.ifEmpty { null }?.let { "_$it" }.orEmpty()
            SaveFileDialog(
                title = string(Strings.ExportDialogTitle),
                extensions = listOf(project.labelerConf.extension),
                initialDirectory = project.currentModule.rawFilePath?.toFile()
                    ?.parent?.toFileOrNull(
                        ensureExists = true,
                        ensureIsDirectory = true,
                    )?.absolutePath
                    ?: project.currentModule.getSampleDirectory(project)
                        .takeIf { it.isDirectory }?.absolutePath,
                initialFileName = project.currentModule.rawFilePath?.lastPathSection
                    ?: project.labelerConf.defaultInputFilePath?.lastPathSection
                    ?: "${project.projectName}$currentModuleNameSection.${project.labelerConf.extension}",
            ) { parent, name ->
                appState.closeExportDialog()
                if (parent != null && name != null) {
                    mainScope.launch(Dispatchers.IO) {
                        appState.showProgress()
                        exportProjectModule(appState.requireProject(), project.currentModuleIndex, File(parent, name))
                        appState.hideProgress()
                    }
                }
            }
        }
        appState.isShowingSampleDirectoryRedirectDialog -> {
            val project = appState.requireProject()
            OpenFileDialog(
                title = string(Strings.ChooseSampleDirectoryDialogTitle),
                initialDirectory = project.currentModule.getSampleDirectory(project)
                    .takeIf { it.isDirectory }?.absolutePath,
                extensions = null,
                directoryMode = true,
            ) { parent, name ->
                appState.closeSampleDirectoryRedirectDialog()
                if (parent != null && name != null) {
                    val newDirectory = File(parent, name).getDirectory()
                    if (newDirectory.exists() && newDirectory.isDirectory) {
                        appState.changeSampleDirectory(newDirectory)
                    }
                }
            }
        }
        appState.isShowingImportDialog -> {
            OpenFileDialog(
                title = string(Strings.ImportDialogTitle),
                extensions = listOf(Project.ProjectFileExtension),
            ) { parent, name ->
                appState.closeImportDialog()
                if (parent != null && name != null) {
                    importProjectFile(mainScope, File(parent, name), appState)
                }
            }
        }
    }
}
