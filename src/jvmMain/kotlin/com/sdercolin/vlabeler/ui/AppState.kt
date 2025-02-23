package com.sdercolin.vlabeler.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sdercolin.vlabeler.audio.Player
import com.sdercolin.vlabeler.audio.PlayerState
import com.sdercolin.vlabeler.env.KeyboardViewModel
import com.sdercolin.vlabeler.exception.InvalidOpenedProjectException
import com.sdercolin.vlabeler.io.awaitLoadProject
import com.sdercolin.vlabeler.io.awaitOpenCreatedProject
import com.sdercolin.vlabeler.io.loadAvailableLabelerConfs
import com.sdercolin.vlabeler.io.loadPlugins
import com.sdercolin.vlabeler.io.loadProject
import com.sdercolin.vlabeler.io.openCreatedProject
import com.sdercolin.vlabeler.io.saveProjectFile
import com.sdercolin.vlabeler.ipc.IpcState
import com.sdercolin.vlabeler.ipc.request.OpenOrCreateRequest
import com.sdercolin.vlabeler.model.AppConf
import com.sdercolin.vlabeler.model.AppRecord
import com.sdercolin.vlabeler.model.LabelerConf
import com.sdercolin.vlabeler.model.MacroPluginExecutionListener
import com.sdercolin.vlabeler.model.Plugin
import com.sdercolin.vlabeler.model.Project
import com.sdercolin.vlabeler.model.SampleInfo
import com.sdercolin.vlabeler.model.action.KeyAction
import com.sdercolin.vlabeler.model.runMacroPlugin
import com.sdercolin.vlabeler.repository.SampleInfoRepository
import com.sdercolin.vlabeler.tracking.TrackingService
import com.sdercolin.vlabeler.tracking.TrackingState
import com.sdercolin.vlabeler.tracking.event.TrackingEvent
import com.sdercolin.vlabeler.tracking.trackMacroPluginExecution
import com.sdercolin.vlabeler.tracking.trackNewAppConf
import com.sdercolin.vlabeler.tracking.trackProjectCreation
import com.sdercolin.vlabeler.tracking.trackTemplateGeneration
import com.sdercolin.vlabeler.ui.dialog.AskIfSaveDialogPurpose
import com.sdercolin.vlabeler.ui.dialog.AskIfSaveDialogResult
import com.sdercolin.vlabeler.ui.dialog.CommonConfirmationDialogAction
import com.sdercolin.vlabeler.ui.dialog.CommonConfirmationDialogResult
import com.sdercolin.vlabeler.ui.dialog.EmbeddedDialogResult
import com.sdercolin.vlabeler.ui.dialog.InputEntryNameDialogArgs
import com.sdercolin.vlabeler.ui.dialog.InputEntryNameDialogPurpose
import com.sdercolin.vlabeler.ui.dialog.InputEntryNameDialogResult
import com.sdercolin.vlabeler.ui.dialog.JumpToEntryDialogResult
import com.sdercolin.vlabeler.ui.dialog.MoveEntryDialogResult
import com.sdercolin.vlabeler.ui.dialog.SetEntryPropertyDialogArgs
import com.sdercolin.vlabeler.ui.dialog.SetEntryPropertyDialogResult
import com.sdercolin.vlabeler.ui.dialog.SetResolutionDialogResult
import com.sdercolin.vlabeler.ui.editor.EditorState
import com.sdercolin.vlabeler.ui.editor.ScrollFitViewModel
import com.sdercolin.vlabeler.ui.string.currentLanguage
import com.sdercolin.vlabeler.util.ParamMap
import com.sdercolin.vlabeler.util.getDefaultNewEntryName
import com.sdercolin.vlabeler.util.toFile
import com.sdercolin.vlabeler.util.toFrame
import com.sdercolin.vlabeler.video.VideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AppState(
    val mainScope: CoroutineScope,
    val keyboardViewModel: KeyboardViewModel,
    val scrollFitViewModel: ScrollFitViewModel,
    val appRecordStore: AppRecordStore,
    private val trackingService: TrackingService,
    snackbarHostState: SnackbarHostState,
    appConf: MutableState<AppConf>,
    availableLabelerConfs: List<LabelerConf>,
    plugins: List<Plugin>,
    progressState: AppProgressState = AppProgressStateImpl(),
    errorState: AppErrorState = AppErrorStateImpl(),
    viewState: AppViewState = AppViewStateImpl(appRecordStore),
    screenState: AppScreenState = AppScreenStateImpl(),
    projectStore: ProjectStore = ProjectStoreImpl(
        mainScope,
        appConf,
        screenState,
        scrollFitViewModel,
        errorState,
        progressState,
    ),
    unsavedChangesState: AppUnsavedChangesState = AppUnsavedChangesStateImpl(),
    snackbarState: AppSnackbarState = AppSnackbarStateImpl(snackbarHostState),
    dialogState: AppDialogState = AppDialogStateImpl(unsavedChangesState, projectStore, snackbarState),
    updaterState: AppUpdaterState = AppUpdaterStateImpl(snackbarState, dialogState, appRecordStore, mainScope),
) : AppErrorState by errorState,
    AppViewState by viewState,
    AppScreenState by screenState,
    ProjectStore by projectStore,
    AppUnsavedChangesState by unsavedChangesState,
    AppSnackbarState by snackbarState,
    AppDialogState by dialogState,
    AppUpdaterState by updaterState,
    AppProgressState by progressState {

    init {
        initDialogState(this)
    }

    var appConf: AppConf by appConf
        private set

    val appRecordFlow: StateFlow<AppRecord> get() = appRecordStore.stateFlow

    var shouldExit: Boolean by mutableStateOf(false)
        private set

    val playerState: PlayerState = PlayerState(appConf.value, mainScope)
    val player: Player get() = playerState.player

    val videoState = VideoState(
        playerState,
        snackbarState,
        exit = { toggleVideoPopup(false) },
    )

    private val ipcState: IpcState = IpcState(this)
    val trackingState = TrackingState(appRecordStore, mainScope)

    fun validate() {
        require(availableLabelerConfs.isNotEmpty()) {
            "No labeler found."
        }
        require(activeLabelerConfs.isNotEmpty()) {
            "No active labeler found."
        }
    }

    private fun reset() {
        clearProject()
        changeScreen(Screen.Starter)
        clearPendingActionAfterSaved()
        projectClosed()
    }

    private val plugins = mutableStateOf(plugins)

    fun getPlugins(type: Plugin.Type) = plugins.value.filter { it.type == type }
    fun getActivePlugins(type: Plugin.Type) = getPlugins(type).filterNot {
        appRecordStore.value.disabledPluginNames.contains(it.name)
    }

    fun reloadPlugins() {
        mainScope.launch(Dispatchers.IO) {
            plugins.value = loadPlugins(appConf.view.language)
        }
    }

    private val _availableLabelerConfs = mutableStateOf(availableLabelerConfs)
    val availableLabelerConfs: List<LabelerConf> get() = _availableLabelerConfs.value
    val activeLabelerConfs: List<LabelerConf>
        get() = availableLabelerConfs.filterNot {
            appRecordStore.value.disabledLabelerNames.contains(it.name)
        }

    fun reloadLabelers() {
        mainScope.launch(Dispatchers.IO) {
            _availableLabelerConfs.value = loadAvailableLabelerConfs()
        }
    }

    private fun changeScreen(screen: Screen) {
        this.screen = screen
        closeAllDialogs()
    }

    fun addRecentProject(file: File) {
        appRecordStore.update { addRecent(file.absolutePath) }
    }

    fun clearRecentProjects() {
        appRecordStore.update { copy(recentProjects = listOf()) }
    }

    fun openEditor(project: Project) {
        runCatching { newProject(project) }
            .onFailure {
                showError(InvalidOpenedProjectException(it))
                return
            }
        SampleInfoRepository.init(project)
        val editor = EditorState(
            project = project,
            appState = this,
        )
        changeScreen(Screen.Editor(editor))
    }

    fun requestOpenProjectCreator() = if (hasUnsavedChanges) askIfSaveBeforeCreateProject() else openProjectCreator()
    private fun askIfSaveBeforeCreateProject() = openEmbeddedDialog(AskIfSaveDialogPurpose.IsCreatingNew)
    private fun openProjectCreator() {
        reset()
        changeScreen(Screen.ProjectCreator)
    }

    fun closeProjectCreator() = reset()

    fun requestCloseProject() = if (hasUnsavedChanges) askIfSaveBeforeCloseProject() else reset()
    private fun askIfSaveBeforeCloseProject() = openEmbeddedDialog(AskIfSaveDialogPurpose.IsClosing)

    fun requestSave(pendingAction: PendingActionAfterSaved? = null) {
        requestProjectSave()
        putPendingActionAfterSaved(pendingAction)
    }

    private fun takeAskIfSaveResult(result: AskIfSaveDialogResult) =
        if (result.save) {
            requestSave(result.actionAfterSaved)
        } else consumePendingActionAfterSaved(result.actionAfterSaved)

    fun notifySaved() {
        projectSaved()
        consumePendingActionAfterSaved(pendingActionAfterSaved)
        discardAutoSavedProjects()
    }

    fun requestCutEntry(index: Int, position: Float, player: Player, sampleInfo: SampleInfo) {
        mainScope.launch {
            val sourceEntry = requireProject().currentModule.entries[index]
            val showSnackbar = { message: String ->
                mainScope.launch { showSnackbar(message) }
                Unit
            }

            val actions = appConf.editor.scissorsActions
            if (actions.play != AppConf.ScissorsActions.Target.None) {
                val startFrame = toFrame(sourceEntry.start, sampleInfo.sampleRate)
                val endFrame = toFrame(sourceEntry.end, sampleInfo.sampleRate)
                val cutFrame = toFrame(position, sampleInfo.sampleRate)
                when (actions.play) {
                    AppConf.ScissorsActions.Target.Former -> {
                        player.playSection(startFrame, cutFrame)
                    }
                    AppConf.ScissorsActions.Target.Latter -> {
                        player.playSection(cutFrame, endFrame)
                    }
                    else -> Unit
                }
            }

            val rename = if (actions.askForName == AppConf.ScissorsActions.Target.Former) {
                val invalidOptions = if (requireProject().labelerConf.allowSameNameEntry) {
                    listOf()
                } else {
                    requireProject().currentModule.entries.map { it.name }.minus(sourceEntry.name)
                }
                val result = awaitEmbeddedDialog(
                    InputEntryNameDialogArgs(
                        index = index,
                        initial = sourceEntry.name,
                        invalidOptions = invalidOptions,
                        showSnackbar = showSnackbar,
                        purpose = InputEntryNameDialogPurpose.CutFormer,
                    ),
                )
                (result as InputEntryNameDialogResult?)?.name ?: return@launch
            } else null
            val newName = if (actions.askForName == AppConf.ScissorsActions.Target.Latter) {
                val invalidOptions = if (requireProject().labelerConf.allowSameNameEntry) {
                    listOf()
                } else {
                    requireProject().currentModule.entries.map { it.name }
                }

                val result = awaitEmbeddedDialog(
                    InputEntryNameDialogArgs(
                        index = index + 1,
                        initial = "",
                        invalidOptions = invalidOptions,
                        showSnackbar = showSnackbar,
                        purpose = InputEntryNameDialogPurpose.CutLatter,
                    ),
                )
                (result as InputEntryNameDialogResult?)?.name ?: return@launch
            } else getDefaultNewEntryName(
                sourceEntry.name,
                requireProject().currentModule.entries.map { it.name },
                requireProject().labelerConf.allowSameNameEntry,
            )
            val targetIndex = when (actions.goTo) {
                AppConf.ScissorsActions.Target.Former -> index
                AppConf.ScissorsActions.Target.Latter -> index + 1
                else -> null
            }
            cutEntry(index, position, rename, newName, targetIndex)
        }
    }

    private fun consumePendingActionAfterSaved(action: PendingActionAfterSaved?) = when (action) {
        is PendingActionAfterSaved.Open -> openOpenProjectDialog()
        is PendingActionAfterSaved.Export -> openExportDialog()
        is PendingActionAfterSaved.ExportOverwrite -> {
            if (action.all) {
                overwriteExportAllModules()
            } else {
                overwriteExportCurrentModule()
            }
        }
        is PendingActionAfterSaved.Close -> reset()
        is PendingActionAfterSaved.CreatingNew -> openProjectCreator()
        is PendingActionAfterSaved.ClearCaches -> clearCachesAndReopen(mainScope)
        is PendingActionAfterSaved.Exit -> exit()
        is PendingActionAfterSaved.OpenRecent -> loadProject(mainScope, action.file, this)
        null -> Unit
    }

    fun handleDialogResult(result: EmbeddedDialogResult<*>) {
        when (result) {
            is SetResolutionDialogResult -> changeResolution(result.newValue)
            is SetEntryPropertyDialogResult -> mainScope.launch(Dispatchers.IO) {
                setCurrentEntryProperty(result.propertyIndex, result.newValue)
            }
            is AskIfSaveDialogResult -> takeAskIfSaveResult(result)
            is JumpToEntryDialogResult -> {
                jumpToEntry(result.index)
            }
            is InputEntryNameDialogResult -> run {
                when (result.purpose) {
                    InputEntryNameDialogPurpose.Rename -> renameEntry(result.index, result.name)
                    InputEntryNameDialogPurpose.Duplicate -> duplicateEntry(result.index, result.name)
                    InputEntryNameDialogPurpose.CutFormer -> {
                        // handled on caller side
                    }
                    InputEntryNameDialogPurpose.CutLatter -> {
                        // handled on caller side
                    }
                }
            }
            is MoveEntryDialogResult -> {
                moveEntry(result.oldIndex, result.newIndex)
            }
            is CommonConfirmationDialogResult -> when (val action = result.action) {
                is CommonConfirmationDialogAction.RemoveCurrentEntry -> removeCurrentEntry()
                is CommonConfirmationDialogAction.LoadAutoSavedProject -> {
                    loadProject(mainScope, action.file, this, autoSaved = true)
                    hasLoadedAutoSavedProject = true
                }
                is CommonConfirmationDialogAction.RedirectSampleDirectory -> {
                    openSampleDirectoryRedirectDialog()
                }
                is CommonConfirmationDialogAction.RemoveCustomizableItem -> {
                    action.state.removeItem(action.item)
                }
            }
            else -> throw NotImplementedError("Dialog result handler is not implemented")
        }
    }

    fun handleErrorPendingAction(action: AppErrorState.ErrorPendingAction?) {
        action ?: return
        when (action) {
            AppErrorState.ErrorPendingAction.Exit -> exit(true)
        }
    }

    fun handleTogglePlayerAction(
        action: KeyAction,
        player: Player,
    ) {
        val sampleInfo = editor?.sampleInfoResult?.getOrNull() ?: return
        if (action == KeyAction.ToggleEntryPlayback) {
            val sampleRate = sampleInfo.sampleRate
            val range = requireProject().currentEntry.run {
                toFrame(start, sampleRate)..toFrame(end, sampleRate)
            }
            player.toggle(range)
        } else {
            player.toggle()
        }
    }

    private fun changeResolution(newValue: Int) {
        editor?.changeResolution(newValue)
    }

    fun openSetPropertyValueDialog(propertyIndex: Int, currentValue: Float) {
        val project = requireProject()
        val property = project.labelerConf.properties[propertyIndex]
        val args = SetEntryPropertyDialogArgs(
            propertyIndex = propertyIndex,
            propertyDisplayedName = property.displayedName,
            currentValue = currentValue,
        )
        openEmbeddedDialog(args)
    }

    fun checkAutoSavedProject() {
        val file = getAutoSavedProjectFile()
        if (file != null) {
            confirmIfLoadAutoSavedProject(file)
        }
    }

    fun checkTrackingPermissionSettings() {
        if (trackingState.hasNotAskedForTrackingPermission()) {
            openTrackingSettingsDialog()
        }
    }

    fun updateAppConf(newConf: AppConf) {
        if (appConf.painter != newConf.painter) {
            mainScope.launch {
                editor?.loadSample(newConf)
            }
        }
        player.loadNewConfIfNeeded(newConf)
        if (appConf.keymaps != newConf.keymaps) {
            mainScope.launch {
                keyboardViewModel.updateKeymaps(newConf.keymaps)
            }
        }
        if (appConf.view.language != newConf.view.language) {
            currentLanguage = newConf.view.language
        }
        appConf = newConf
        trackNewAppConf(newConf)
    }

    fun requestExit() = if (hasUnsavedChanges) askIfSaveBeforeExit() else exit()

    fun exit(fromError: Boolean = false) {
        mainScope.launch {
            terminateAutoSaveProject()
            if (!fromError) discardAutoSavedProjects()
            ipcState.close()
            shouldExit = true
        }
    }

    val isEditorActive: Boolean
        get() {
            val editor = screen as? Screen.Editor ?: return false
            return project != null &&
                editor.state.isPinnedEntryListInputFocused.not() &&
                editor.state.isEditingTag.not() &&
                !anyDialogOpening()
        }

    val isMacroPluginAvailable
        get() = project != null && screen is Screen.Editor && !anyDialogOpeningExceptMacroPluginManager()

    val isScrollFitEnabled get() = editor?.sampleInfoResult?.exceptionOrNull() == null

    private val macroPluginExecutionListener = MacroPluginExecutionListener(
        onReport = { showMacroPluginReport(it) },
        onAudioPlaybackRequest = { player.handleRequest(it) },
    )

    fun executeMacroPlugin(plugin: Plugin, params: ParamMap, slot: Int?) {
        val newProject = runCatching { runMacroPlugin(plugin, params, requireProject(), macroPluginExecutionListener) }
            .getOrElse {
                showError(it)
                return
            }
        editProject { newProject }
        trackMacroPluginExecution(plugin, params, quickLaunch = slot != null)
    }

    fun consumeOpenOrCreateIpcRequest(request: OpenOrCreateRequest) = mainScope.launch {
        // check if the project is already opened
        val isProjectAlreadyOpened = request.projectFile.toFile().absolutePath == project?.projectFile?.absolutePath
        if (!isProjectAlreadyOpened) {
            // check if the project exists
            val isProjectExisting = request.projectFile.toFile().exists()

            // save current project if it is not saved
            if (project != null && hasUnsavedChanges) {
                val purpose = if (isProjectExisting) {
                    AskIfSaveDialogPurpose.IsOpening
                } else {
                    AskIfSaveDialogPurpose.IsCreatingNew
                }
                val result = awaitEmbeddedDialog(purpose) as AskIfSaveDialogResult?
                when {
                    result == null -> {
                        // canceled, the request is discarded
                        return@launch
                    }
                    result.save -> {
                        saveProjectFile(project)
                    }
                }
            }
            if (isProjectExisting) {
                // load the project
                awaitLoadProject(request.projectFile.toFile(), this@AppState)
            } else {
                // create a new project
                val newProject = request.newProjectArgs.create(
                    projectFile = request.projectFile.toFile(),
                    availableLabelers = activeLabelerConfs,
                    availableTemplatePlugins = getActivePlugins(Plugin.Type.Template),
                ).getOrElse {
                    showError(it)
                    return@launch
                }
                trackProjectCreation(newProject, byIpcRequest = true)
                awaitOpenCreatedProject(newProject, this@AppState)
            }
        }

        // go to the entry
        request.gotoEntryByIndex?.let {
            jumpToModuleByNameAndEntry(it.parentFolderName, it.entryIndex)
            return@launch
        }
        request.gotoEntryByName?.let {
            jumpToModuleByNameAndEntryName(it.parentFolderName, it.entryName)
        }
    }

    fun onCreateProject(project: Project, plugin: Plugin?, pluginParams: ParamMap?) {
        trackProjectCreation(project, byIpcRequest = false)
        if (plugin != null) {
            trackTemplateGeneration(plugin, pluginParams)
        }
        openCreatedProject(mainScope, project, this)
    }

    fun track(event: TrackingEvent) = trackingService.track(event)

    sealed class PendingActionAfterSaved {
        object Open : PendingActionAfterSaved()
        class OpenRecent(val file: File) : PendingActionAfterSaved()
        object Export : PendingActionAfterSaved()
        class ExportOverwrite(val all: Boolean) : PendingActionAfterSaved()
        object Close : PendingActionAfterSaved()
        object CreatingNew : PendingActionAfterSaved()
        object ClearCaches : PendingActionAfterSaved()
        object Exit : PendingActionAfterSaved()
    }
}
