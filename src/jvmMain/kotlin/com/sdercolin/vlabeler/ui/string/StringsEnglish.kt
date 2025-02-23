package com.sdercolin.vlabeler.ui.string

import com.sdercolin.vlabeler.ui.string.Strings.*

fun Strings.en(): String = when (this) {
    AppName -> "vLabeler"
    MenuFile -> "File"
    MenuFileNewProject -> "New Project..."
    MenuFileOpen -> "Open..."
    MenuFileOpenRecent -> "Open Recent"
    MenuFileOpenRecentClear -> "Clear Recently Opened"
    MenuFileSave -> "Save"
    MenuFileSaveAs -> "Save As..."
    MenuFileProjectSetting -> "Project Settings..."
    MenuFileImport -> "Import..."
    MenuFileExport -> "Export..."
    MenuFileExportOverwrite -> "Export Overwriting"
    MenuFileExportOverwriteAll -> "Export Overwriting All"
    MenuFileInvalidateCaches -> "Invalidate Caches"
    MenuFileClose -> "Close"
    MenuEdit -> "Edit"
    MenuEditUndo -> "Undo"
    MenuEditRedo -> "Redo"
    MenuEditTools -> "Tools"
    MenuEditToolsCursor -> "Cursor"
    MenuEditToolsScissors -> "Scissors"
    MenuEditToolsPan -> "Pan"
    MenuEditRenameEntry -> "Rename Current Entry..."
    MenuEditDuplicateEntry -> "Duplicate Current Entry..."
    MenuEditRemoveEntry -> "Remove Current Entry"
    MenuEditMoveEntry -> "Move Current Entry to..."
    MenuEditToggleDone -> "Toggle Done for Current Entry"
    MenuEditToggleStar -> "Toggle Starred for Current Entry"
    MenuEditEditTag -> "Edit Tag of Current Entry"
    MenuEditMultipleEditMode -> "Edit All Connected Entries"
    MenuView -> "View"
    MenuViewToggleMarker -> "Show Parameter Controllers"
    MenuViewPinEntryList -> "Pin Entry List"
    MenuViewPinEntryListLocked -> "Lock Splitter for Pinned Entry List"
    MenuViewToggleProperties -> "Show Properties"
    MenuViewToggleToolbox -> "Show Toolbox"
    MenuViewOpenSampleList -> "Sample List"
    MenuViewVideo -> "Display Related Video"
    MenuViewVideoOff -> "Off"
    MenuViewVideoEmbedded -> "Embedded"
    MenuViewVideoNewWindow -> "In New Window"
    MenuNavigate -> "Navigate"
    MenuNavigateOpenLocation -> "Open Locations"
    MenuNavigateOpenLocationRootDirectory -> "Root Sample Directory"
    MenuNavigateOpenLocationModuleDirectory -> "Sample Directory of Current Subproject"
    MenuNavigateOpenLocationProjectLocation -> "Project File Location"
    MenuNavigateNextEntry -> "Go to Next Entry"
    MenuNavigatePreviousEntry -> "Go to Previous Entry"
    MenuNavigateNextSample -> "Go to Next Sample"
    MenuNavigatePreviousSample -> "Go to Previous Sample"
    MenuNavigateNextModule -> "Go to Next Subproject"
    MenuNavigatePreviousModule -> "Go to Previous Subproject"
    MenuNavigateJumpToEntry -> "Go to Entry..."
    MenuNavigateScrollFit -> "Scroll to Show the Current Entry"
    MenuTools -> "Tools"
    MenuToolsBatchEdit -> "Batch Edit"
    MenuToolsBatchEditQuickLaunchManager -> "Slot Settings..."
    MenuToolsBatchEditQuickLaunch -> "Slot %d: %s"
    MenuToolsBatchEditShowDisabledItems -> "Show plugins that could not be used under the current project"
    MenuToolsBatchEditManagePlugins -> "Manage Plugins..."
    MenuToolsPrerender -> "Prerender All Charts..."
    MenuToolsRecycleMemory -> "Recycle Memory"
    MenuSettings -> "Settings"
    MenuSettingsPreferences -> "Preferences..."
    MenuSettingsLabelers -> "Labelers..."
    MenuSettingsTemplatePlugins -> "Template Generators..."
    MenuSettingsTracking -> "Track App Usages..."
    MenuHelp -> "Help"
    MenuHelpCheckForUpdates -> "Check for Updates..."
    MenuHelpOpenLogDirectory -> "Open Log Directory"
    MenuHelpOpenLatestRelease -> "Open Latest Release"
    MenuHelpOpenGitHub -> "Open GitHub Page"
    MenuHelpJoinDiscord -> "Join Discord"
    MenuHelpAbout -> "About"
    CommonOkay -> "OK"
    CommonApply -> "Apply"
    CommonCancel -> "Cancel"
    CommonYes -> "Yes"
    CommonNo -> "No"
    CommonWarning -> "Warning"
    CommonError -> "Error"
    CommonDetails -> "Details"
    CommonOthers -> "Others"
    CommonPrevious -> "Previous"
    CommonNext -> "Next"
    CommonFinish -> "Finish"
    CommonInputErrorPromptNumber -> "Please enter a number."
    CommonInputErrorPromptInteger -> "Please enter an integer number."
    CommonInputErrorPromptNumberRange -> "Please enter a number between %s and %s."
    CommonInputErrorPromptNumberMin -> "Please enter a number greater than or equal to %s."
    CommonInputErrorPromptNumberMax -> "Please enter a number less than or equal to %s."
    CommonRootModuleName -> "(Root)"
    StarterStart -> "Start"
    StarterNewProject -> "New Project..."
    StarterOpen -> "Open..."
    StarterRecent -> "Recent"
    StarterRecentEmpty -> "You have no recent projects."
    StarterRecentDeleted -> "This project file has been removed."
    StarterNewSampleDirectory -> "Sample directory"
    StarterNewWorkingDirectory -> "Project location"
    StarterNewProjectTitle -> "New Project"
    StarterNewProjectName -> "Project name"
    StarterNewProjectNameWarning -> "The project file already exists. Creating the project will overwrite the file."
    StarterNewCacheDirectory -> "Cache directory"
    StarterNewLabelerCategory -> "Category"
    StarterNewLabeler -> "Labeler"
    StarterNewTemplatePlugin -> "Template generator"
    StarterNewTemplatePluginNone -> "None"
    StarterNewInputFile -> "Input file (.%s)"
    StarterNewEncoding -> "Encoding"
    StarterNewAutoExport -> "Auto-export"
    StarterNewAutoExportHelp ->
        "Automatically export the project to overwrite the given input file (if not given, " +
            "the default file position defined by labeler) when the project is saved."
    StarterNewWarningSelfConstructedLabelerWithTemplatePlugin ->
        "You are trying to use a template generator with a labeler that manages multiple subprojects. " +
            "This is dangerous because it may overwrite all your existing label files with the generated entries. " +
            "Please double confirm that the settings meet your needs."
    StarterNewDirectoryPage -> "Directory Settings"
    StarterNewLabelerPage -> "Labeler Settings"
    StarterNewDataSourcePage -> "Data Source Settings"
    StarterNewContentType -> "Create by..."
    StarterNewContentTypeDefault -> "Default"
    StarterNewContentTypeFile -> "File"
    StarterNewContentTypePlugin -> "Template generator"
    StarterNewAdvancedSettings -> "Advanced Settings"
    SampleListIncludedHeader -> "Project sample files"
    SampleListIncludedItemEntryCountSingle -> "%d entry"
    SampleListIncludedItemEntryCountPlural -> "%d entries"
    SampleListExcludedHeader -> "Other sample files"
    SampleListExcludedPlaceholder -> "There are no unreferenced sample files in the sample directory."
    SampleListEntryHeader -> "Entries"
    SampleListEntriesPlaceholderUnselected -> "Select a sample file on the left to show entries bound to it."
    SampleListEntriesPlaceholderNoEntry -> "There are no entries bound to the selected sample file."
    SampleListEntriesPlaceholderNoEntryButton -> "Create Default"
    SampleListJumpToSelectedEntryButton -> "Go to selected entry"
    SampleListOpenSampleDirectoryButton -> "Open sample directory"
    SampleListCurrentModuleLabel -> "Subproject: "
    SampleListSampleDirectoryLabel -> "Sample directory: "
    SampleListSampleDirectoryRedirectButton -> "Change sample directory"
    PrerendererModuleText -> "Rendering subprojects %d/%d..."
    PrerendererModuleTextFinished -> "Rendering subprojects %d/%d... Done"
    PrerendererSampleText -> "Rendering sample files %d/%d..."
    PrerendererSampleTextFinished -> "Rendering sample files %d/%d... Done"
    PrerendererChartText -> "Rendering charts %d/%d..."
    PrerendererChartTextFinished -> "Rendering charts %d/%d... Done"
    EditorRenderStatusLabel -> "%d/%d Rendering..."
    ChooseSampleDirectoryDialogTitle -> "Choose sample directory"
    ChooseWorkingDirectoryDialogTitle -> "Choose project location directory"
    ChooseCacheDirectoryDialogTitle -> "Choose cache directory"
    ChooseInputFileDialogTitle -> "Choose input file"
    OpenProjectDialogTitle -> "Open project"
    SaveAsProjectDialogTitle -> "Save as project"
    ImportDialogTitle -> "Import"
    ExportDialogTitle -> "Export"
    SetResolutionDialogDescription -> "Input canvas resolution (points per pixel) for the editor (%d ~ %d)"
    SetEntryPropertyDialogDescription ->
        "Please enter the value of the property `%s` of this entry. \n" +
            "Please note that your input value will not be restricted, " +
            "so please check the data validity by yourself after this process."
    AskIfSaveBeforeOpenDialogDescription ->
        "You have unsaved changes. Do you want to save them before opening a " +
            "new project?"
    AskIfSaveBeforeExportDialogDescription -> "You have unsaved changes. Do you want to save them before exporting?"
    AskIfSaveBeforeCloseDialogDescription ->
        "You have unsaved changes. Do you want to save them before closing the " +
            "current project?"
    AskIfSaveBeforeExitDialogDescription -> "You have unsaved changes. Do you want to save them before exiting?"
    InputEntryNameDialogDescription -> "Rename entry"
    InputEntryNameDuplicateDialogDescription -> "Input name for the new entry"
    InputEntryNameCutFormerDialogDescription -> "Input name for the former entry after cutting"
    InputEntryNameCutLatterDialogDescription -> "Input name for the latter entry after cutting"
    EditEntryNameDialogExistingError -> "The name you input already exists."
    MoveEntryDialogDescription -> "Input new index for entry \"%1\$s\" (%2\$d ~ %3\$d)"
    AskIfRemoveEntryDialogDescription -> "Removing current entry..."
    AskIfRemoveEntryLastDialogDescription ->
        "Removing current entry...\n" +
            "This entry is the only one that has reference of the current sample file.\n" +
            "If you need to add an entry on it later, please see menu `View` -> `Sample List`."
    AskIfLoadAutoSavedProjectDialogDescription ->
        "Auto-saved project file found. Do you want to load it? " +
            "The file will be discarded if you opened or created another one."
    AskIfRedirectSampleDirectoryDialogDescription ->
        "The sample directory of the current subproject (%s) is not found, " +
            "or doesn't contain any required sample files." +
            "Do you want to redirect it to a new directory?"
    PluginDialogTitle -> "vLabeler - Plugin"
    PluginDialogInfoAuthor -> "author: %s"
    PluginDialogInfoVersion -> "version: %d"
    PluginDialogInfoContact -> "Contact author"
    PluginDialogDescriptionMin -> "min: %s"
    PluginDialogDescriptionMax -> "max: %s"
    PluginDialogDescriptionMinMax -> "min: %s, max: %s"
    PluginDialogExecute -> "Execute"
    PluginDialogImportFromSavedParams -> "Load default saved parameters"
    PluginDialogImportFromSlot -> "Load from slot %1\$d: %2\$s"
    PluginDialogEmptySlotName -> "(empty)"
    PluginDialogImportFromFile -> "Import from file"
    PluginDialogImportSuccess -> "Successfully imported the preset."
    PluginDialogImportFailure -> "Failed to import the preset. It's probably incompatible with the current item."
    PluginDialogExportToSavedParams -> "Save parameters as default"
    PluginDialogExportToSlot -> "Save parameters to slot %1\$d: %2\$s"
    PluginDialogExportToFile -> "Export parameters to file"
    PluginDialogExportSuccess -> "Successfully exported the preset."
    PluginDialogExportFailure -> "Failed to export the preset."
    PluginEntrySelectorTextMatchTypeEquals -> "Equals"
    PluginEntrySelectorTextMatchTypeContains -> "Contains"
    PluginEntrySelectorTextMatchTypeStartsWith -> "Starts with"
    PluginEntrySelectorTextMatchTypeEndsWith -> "Ends with"
    PluginEntrySelectorTextMatchTypeRegex -> "Regex"
    PluginEntrySelectorNumberMatchTypeEquals -> "="
    PluginEntrySelectorNumberMatchTypeGreaterThan -> ">"
    PluginEntrySelectorNumberMatchTypeGreaterThanOrEquals -> ">="
    PluginEntrySelectorNumberMatchTypeLessThan -> "<"
    PluginEntrySelectorNumberMatchTypeLessThanOrEquals -> "<="
    PluginEntrySelectorPreservedSubjectSample -> "Sample name (no extension)"
    PluginEntrySelectorPreservedSubjectName -> "Entry name"
    PluginEntrySelectorPreservedSubjectTag -> "Tag"
    PluginEntrySelectorPreservedSubjectDone -> "Done"
    PluginEntrySelectorPreservedSubjectStar -> "Starred"
    PluginEntrySelectorComparerValue -> "Input"
    PluginEntrySelectorPreviewSummaryError -> "Invalid input"
    PluginEntrySelectorPreviewSummaryInitializing -> "Initializing..."
    PluginEntrySelectorPreviewSummary -> "Selecting %d/%d"
    PluginEntrySelectorPlaceholder -> "No filters, selecting all entries."
    EditorSubTitleMultiple -> "editing %1\$d entries in sample %2\$s"
    FailedToLoadSampleFileError -> "Could not load the sample file.\nIt may not exist or is not a supported format."
    PluginRuntimeUnexpectedException ->
        "An unexpected error occurred during the plugin execution.\n" +
            "Please contact the author for more information."
    InvalidCreatedProjectException ->
        "The created project is not valid." +
            "Please check the documentations of the labeler/plugin to ensure your settings are correct.\n" +
            "If the problem still occurs, please contact the author of the labeler/plugin for more information."
    InvalidOpenedProjectException ->
        "Could not open the project because it contains invalid data.\n" +
            "Please check the error log for more information."
    ProjectParseException ->
        "Could not open the project.\n" +
            "It may be corrupted or created by an incompatible version of vLabeler.\n" +
            "Please try to create a new project and import the project file using menu `File` -> `Import...`."
    ProjectImportException ->
        "Could not import the file.\n" +
            "It may he an invalid vLabeler project file. Please check the error log for more information."
    ProjectUpdateOnSampleException ->
        "Could not update the project with the loaded sample file." +
            "\nPlease check the error log for more information."
    InvalidEditedProjectException -> "Invalid edited project.\nPlease check error log for more information."
    CustomizableItemLoadingException -> "Could not load the selected customized item."
    PluginRuntimeExceptionTemplate -> "Plugin runtime error: %s"
    VideoComponentInitializationException ->
        "Could not initialize the video component. You need to install VLC on your device to use this feature. " +
            "Please read the `Video integration` section in README for details."
    VideoFileNotFoundExceptionTemplate ->
        "Video not found by the same name of \"%s\" and extension among %s."
    LabelerManagerTitle -> "Labelers"
    LabelerManagerImportDialogTitle -> "Import labeler"
    TemplatePluginManagerTitle -> "Template generators"
    TemplatePluginManagerImportDialogTitle -> "Import template generator"
    MacroPluginManagerTitle -> "Batch Edit Plugins"
    MacroPluginManagerImportDialogTitle -> "Import batch edit plugin"
    MacroPluginReportDialogTitle -> "Batch edit execution result"
    MacroPluginReportDialogCopy -> "Copy"
    CustomizableItemManagerRemoveItemConfirm ->
        "Are you sure you want to remove \"%s\"? " +
            "This will remove the file(s) from the disk."
    CustomizableItemManagerOpenDirectory -> "Open directory"
    CustomizableItemManagerReload -> "Reload list"
    CustomizableItemManagerLockedDescription -> "This item is built-in and cannot be removed."
    PreferencesEditorImport -> "Import"
    PreferencesEditorImportDialogTitle -> "Import preferences"
    PreferencesEditorImportSuccess -> "Successfully imported preferences."
    PreferencesEditorImportFailure -> "Failed to import the selected preferences file."
    PreferencesEditorExport -> "Export"
    PreferencesEditorExportSuccess -> "Successfully exported preferences."
    PreferencesEditorExportFailure -> "Failed to export preferences to the selected file."
    PreferencesEditorExportDialogTitle -> "Export preferences"
    PreferencesEditorResetPage -> "Reset items in this page"
    PreferencesEditorResetAll -> "Reset all items"
    PreferencesCharts -> "Charts"
    PreferencesChartsDescription -> "Customize the charts being rendered in the editor."
    PreferencesChartsCanvas -> "Canvas"
    PreferencesChartsCanvasDescription -> "Customize general settings about the canvas where the charts are drawn."
    PreferencesChartsCanvasResolution -> "Canvas resolution"
    PreferencesChartsCanvasResolutionDescription ->
        "Defined as the number of sample points included in 1 pixel.\n" +
            "The bigger the number, the longer time duration the charts show on your screen."
    PreferencesChartsCanvasResolutionDefault -> "Default resolution"
    PreferencesChartsCanvasResolutionStep -> "Step"
    PreferencesChartsMaxDataChunkSize -> "Max data chunk size"
    PreferencesChartsMaxDataChunkSizeDescription ->
        "Max frames that will be included in a chart chunk.\n" +
            "The bigger the number, the fewer parts your charts will be divided into during rendering."
    PreferencesChartsWaveform -> "Waveform"
    PreferencesChartsWaveformDescription -> "Customize the waveform chart."
    PreferencesChartsWaveformResampleDownTo -> "Maximum sample rate (Hz)"
    PreferencesChartsWaveformResampleDownToDescription ->
        "Audio file with a higher sample rate " +
            "will be resampled down to the given value (the files are not edited.)\n" +
            "Set to 0 to disable resampling."
    PreferencesChartsWaveformNormalize -> "Normalize audio"
    PreferencesChartsWaveformNormalizeDescription ->
        "Normalization takes extra time when loading samples for the first time."
    PreferencesChartsWaveformUnitSize -> "Points per pixel"
    PreferencesChartsWaveformUnitSizeDescription -> "Increase for lower image quality."
    PreferencesChartsWaveformIntensityAccuracy -> "Bitmap height (px)"
    PreferencesChartsWaveformYAxisBlankRate -> "Vertical padding (%%)"
    PreferencesChartsWaveformColor -> "Color"
    PreferencesChartsWaveformBackgroundColor -> "Background color"
    PreferencesChartsSpectrogram -> "Spectrogram"
    PreferencesChartsSpectrogramDescription -> "Customize the spectrogram chart."
    PreferencesChartsSpectrogramEnabled -> "Show spectrogram"
    PreferencesChartsSpectrogramHeight -> "Height relative to waveforms (%%)"
    PreferencesChartsSpectrogramPointDensity -> "Points per pixel"
    PreferencesChartsSpectrogramPointDensityDescription -> "Increase for lower image quality."
    PreferencesChartsSpectrogramHopSize -> "FFT hop size"
    PreferencesChartsSpectrogramHopSizeDescription -> "Adapted to the actual sample rate."
    PreferencesChartsSpectrogramWindowSize -> "Window size"
    PreferencesChartsSpectrogramWindowSizeDescription -> "Adapted to the actual sample rate."
    PreferencesChartsSpectrogramMelScaleStep -> "Frequency resolution (mel)"
    PreferencesChartsSpectrogramMaxFrequency -> "Max frequency displayed (Hz)"
    PreferencesChartsSpectrogramMinIntensity -> "Min intensity displayed (dB)"
    PreferencesChartsSpectrogramMaxIntensity -> "Max intensity displayed (dB)"
    PreferencesChartsSpectrogramWindowType -> "Window function"
    PreferencesChartsSpectrogramColorPalette -> "Colors"
    PreferencesKeymap -> "Keymap"
    PreferencesKeymapDescription -> "Customize key bindings for key/mouse actions."
    PreferencesKeymapKeyAction -> "Key actions"
    PreferencesKeymapKeyActionDescription -> "Customize key bindings for key actions."
    PreferencesKeymapMouseClickAction -> "Mouse click actions"
    PreferencesKeymapMouseClickActionDescription ->
        "Customize key bindings for mouse click actions.\n" +
            "An action is conducted only while all the keys in the key bindings are pressed."
    PreferencesKeymapMouseScrollAction -> "Mouse scroll actions"
    PreferencesKeymapMouseScrollActionDescription ->
        "Customize key bindings for mouse scroll actions.\n" +
            "An action is conducted only while all the keys in the key bindings are pressed."
    PreferencesKeymapEditDialogTitle -> "Editing key bind for:"
    PreferencesKeymapEditDialogDescriptionMouseClick ->
        "Left/Right click on the text field with other " +
            "keys pressed to input a shortcut."
    PreferencesKeymapEditDialogDescriptionMouseScroll ->
        "Scroll mouse wheel on the text field with other " +
            "keys pressed to input a shortcut."
    PreferencesKeymapEditDialogConflictingLabel -> "Already assigned to:"
    PreferencesKeymapEditDialogConflictingWarning ->
        "This shortcut is already assigned to other actions.\n" +
            "Do you want to remove the other assignments?"
    PreferencesKeymapEditDialogConflictingWarningKeep -> "Keep"
    PreferencesKeymapEditDialogConflictingWarningRemove -> "Remove"
    PreferencesView -> "View"
    PreferencesViewDescription -> "Customize view appearances"
    PreferencesViewLanguage -> "Language"
    PreferencesViewHideSampleExtension -> "Hide sample file extension"
    PreferencesViewAppAccentColor -> "Accent color (light) of the app"
    PreferencesViewAppAccentColorVariant -> "Accent color (dark) of the app"
    PreferencesViewPinnedEntryListPosition -> "Position of pinned entry list"
    PreferencesViewPositionLeft -> "Left"
    PreferencesViewPositionRight -> "Right"
    PreferencesViewPositionTop -> "Top"
    PreferencesViewPositionBottom -> "Bottom"
    PreferencesViewCornerPositionTopLeft -> "Top left"
    PreferencesViewCornerPositionTopRight -> "Top right"
    PreferencesViewCornerPositionBottomLeft -> "Bottom left"
    PreferencesViewCornerPositionBottomRight -> "Bottom right"
    PreferencesFontSizeSmall -> "Small"
    PreferencesFontSizeMedium -> "Medium"
    PreferencesFontSizeLarge -> "Large"
    PreferencesEditor -> "Editor"
    PreferencesEditorDescription -> "Customize the editor's appearance and behavior."
    PreferencesEditorPlayerCursorColor -> "Player cursor color"
    PreferencesEditorPlayerLockedDrag -> "Fixed-drag"
    PreferencesEditorPlayerLockedDragDescription ->
        "Select a condition to enable fixed-drag while you move " +
            "the parameter lines.\n" +
            "When it is enabled, the other parameter lines will be moved accordingly to " +
            "keep relative positions to the parameter you are moving."
    PreferencesEditorPlayerLockedDragUseLabeler -> "Use settings defined by the labeler"
    PreferencesEditorPlayerLockedDragUseStart -> "Fixed-drag at the entry's start"
    PreferencesEditorPlayerLockedDragNever -> "Never do fixed-drag"
    PreferencesEditorPlayerLockedSettingParameterWithCursor -> "Apply fixed-drag with cursor setting"
    PreferencesEditorPlayerLockedSettingParameterWithCursorDescription ->
        "Apply the fixed-drag setting above also when " +
            "setting the parameters with \"Set Parameter To Cursor Position\" key actions"
    PreferencesEditorNotes -> "Notes"
    PreferencesEditorNotesDescription ->
        "Customize the editor's appearance and behavior about entry " +
            "notes (starred, done, tag)."
    PreferencesEditorShowDone -> "Display \"Done\" status"
    PreferencesEditorAutoDone -> "Automatically set edited entries as \"Done\""
    PreferencesEditorShowStarred -> "Display \"Starred\" status"
    PreferencesEditorShowTag -> "Display tags"
    PreferencesEditorScissors -> "Scissors"
    PreferencesEditorScissorsDescription -> "Customize appearance and behavior of the scissors tool."
    PreferencesEditorScissorsColor -> "Color"
    PreferencesEditorScissorsActionTargetNone -> "None"
    PreferencesEditorScissorsActionTargetFormer -> "The former entry"
    PreferencesEditorScissorsActionTargetLatter -> "The latter entry"
    PreferencesEditorScissorsActionGoTo -> "Go to entry after cutting"
    PreferencesEditorScissorsActionAskForName -> "Rename entry after cutting"
    PreferencesEditorScissorsActionPlay -> "Play audio when cutting"
    PreferencesEditorAutoScroll -> "Auto scroll"
    PreferencesEditorAutoScrollDescription ->
        "Define when the editor will automatically scroll to show " +
            "the current entry."
    PreferencesEditorAutoScrollOnLoadedNewSample -> "When switched to another sample"
    PreferencesEditorAutoScrollOnJumpedToEntry -> "When switched to another entry by absolute index"
    PreferencesEditorAutoScrollOnSwitchedInMultipleEditMode -> "When switched to another entry in multiple edit mode"
    PreferencesEditorAutoScrollOnSwitched -> "When switched to another entry"
    PreferencesEditorContinuousLabelNames -> "Label names (continuous)"
    PreferencesEditorContinuousLabelNamesDescription ->
        "Customize appearance of entry name texts shown in the editor, when using a continuous labeler."
    PreferencesEditorContinuousLabelNamesColor -> "Color"
    PreferencesEditorContinuousLabelNamesSize -> "Size"
    PreferencesEditorContinuousLabelNamesPosition -> "Position"
    PreferencesPlayback -> "Playback"
    PreferencesPlaybackDescription -> "Customize the behavior about audio playback."
    PreferencesPlaybackPlayOnDragging -> "Preview playback"
    PreferencesPlaybackPlayOnDraggingDescription ->
        "When dragging any parameter lines with the keys for \"Preview playback\" (see keymap), " +
            "play the audio range near the cursor."
    PreferencesPlaybackPlayOnDraggingEnabled -> "Enabled"
    PreferencesPlaybackPlayOnDraggingRangeRadiusMillis -> "Radius (ms)"
    PreferencesPlaybackPlayOnDraggingEventQueueSize -> "Retain drag events"
    PreferencesAutoSave -> "Auto save"
    PreferencesAutoSaveDescription -> "Customize the behavior about project auto-save."
    PreferencesAutoSaveTarget -> "Location of auto-saved file"
    PreferencesAutoSaveTargetNone -> "Do not auto-save"
    PreferencesAutoSaveTargetProject -> "Overwrite project file"
    PreferencesAutoSaveTargetRecord -> "Save to temporary file"
    PreferencesAutoSaveIntervalSec -> "Interval (sec)"
    PreferencesHistory -> "Edit history"
    PreferencesHistoryDescription -> "Customize the behavior about edit history (undo/redo)."
    PreferencesHistoryMaxSize -> "Maximum retained size"
    PreferencesHistorySquashIndex -> "Squash index changes"
    PreferencesHistorySquashIndexDescription ->
        "When enabled, index changes (e.g. switch entries) will not be saved " +
            "until the next content change."
    ActionToggleSamplePlayback -> "Toggle Playback of Current Sample"
    ActionToggleEntryPlayback -> "Toggle Playback of Current Entry"
    ActionToggleVideoPopupEmbedded -> "Toggle Video Display (Embedded)"
    ActionToggleVideoPopupNewWindow -> "Toggle Video Display (In New Window)"
    ActionIncreaseResolution -> "Zoom Out"
    ActionDecreaseResolution -> "Zoom In"
    ActionInputResolution -> "Input Canvas Resolution"
    ActionCancelDialog -> "Close Dialog"
    ActionScissorsCut -> "Cut at Current Cursor Position"
    ActionSetValue1 -> "Set Parameter 1 To Cursor Position"
    ActionSetValue2 -> "Set Parameter 2 To Cursor Position"
    ActionSetValue3 -> "Set Parameter 3 To Cursor Position"
    ActionSetValue4 -> "Set Parameter 4 To Cursor Position"
    ActionSetValue5 -> "Set Parameter 5 To Cursor Position"
    ActionSetValue6 -> "Set Parameter 6 To Cursor Position"
    ActionSetValue7 -> "Set Parameter 7 To Cursor Position"
    ActionSetValue8 -> "Set Parameter 8 To Cursor Position"
    ActionSetValue9 -> "Set Parameter 9 To Cursor Position"
    ActionSetValue10 -> "Set Parameter 10 To Cursor Position"
    ActionSetProperty1 -> "Input Value for Property 1"
    ActionSetProperty2 -> "Input Value for Property 2"
    ActionSetProperty3 -> "Input Value for Property 3"
    ActionSetProperty4 -> "Input Value for Property 4"
    ActionSetProperty5 -> "Input Value for Property 5"
    ActionSetProperty6 -> "Input Value for Property 6"
    ActionSetProperty7 -> "Input Value for Property 7"
    ActionSetProperty8 -> "Input Value for Property 8"
    ActionSetProperty9 -> "Input Value for Property 9"
    ActionSetProperty10 -> "Input Value for Property 10"
    ActionQuickLaunch1 -> "Launch Plugin Slot 1"
    ActionQuickLaunch2 -> "Launch Plugin Slot 2"
    ActionQuickLaunch3 -> "Launch Plugin Slot 3"
    ActionQuickLaunch4 -> "Launch Plugin Slot 4"
    ActionQuickLaunch5 -> "Launch Plugin Slot 5"
    ActionQuickLaunch6 -> "Launch Plugin Slot 6"
    ActionQuickLaunch7 -> "Launch Plugin Slot 7"
    ActionQuickLaunch8 -> "Launch Plugin Slot 8"
    ActionMoveParameter -> "Drag Parameter Line"
    ActionMoveParameterWithPlaybackPreview -> "Drag Parameter Line with Playback Preview"
    ActionMoveParameterIgnoringConstraints -> "Drag Parameter Line Ignoring Constraints"
    ActionMoveParameterInvertingLocked -> "Drag Parameter Line with Fixed-drag Settings Inverted"
    ActionPlayAudioSection -> "Play the Clicked Audio Part"
    ActionScrollCanvasLeft -> "Scroll Canvas to Left"
    ActionScrollCanvasRight -> "Scroll Canvas to Right"
    ActionZoomInCanvas -> "Zoom In"
    ActionZoomOutCanvas -> "Zoom Out"
    ActionGoToNextEntry -> "Go to Next Entry"
    ActionGoToPreviousEntry -> "Go to Previous Entry"
    ActionGoToNextSample -> "Go to Next Sample"
    ActionGoToPreviousSample -> "Go to Previous Sample"
    CheckForUpdatesAlreadyUpdated -> "You already have the latest version of vLabeler installed."
    CheckForUpdatesFailure -> "Could not fetch the latest version information."
    UpdaterDialogSummaryDetailsLink -> "Details"
    UpdaterDialogTitle -> "vLabeler - Update"
    UpdaterDialogCurrentVersionLabel -> "Current version: %s"
    UpdaterDialogLatestVersionLabel -> "Latest version: %1\$s (%2\$s)"
    UpdaterDialogStartDownloadButton -> "Download"
    UpdaterDialogIgnoreButton -> "Ignore this version"
    UpdaterDialogDownloadPositionLabel -> "Download location: "
    UpdaterDialogChangeDownloadPositionButton -> "Change"
    UpdaterDialogChooseDownloadPositionDialogTitle -> "Choose download location"
    AboutDialogTitle -> "vLabeler - About"
    AboutDialogCopyInfo -> "Copy info"
    AboutDialogShowLicenses -> "Show licenses"
    LicenseDialogTitle -> "vLabeler - Licenses"
    LicenseDialogLicenses -> "OSS Licenses used in vLabeler"
    LoadProjectWarningLabelerCreated -> "A new labeler `%s` is installed by the project file."
    LoadProjectWarningLabelerUpdated -> "Labeler `%s` is updated to version `%s` by the project file."
    LoadProjectWarningCacheDirReset ->
        "Cannot find or create the cache directory defined in the project file. " +
            "A default cache directory will be used instead."
    FilterStarred -> "Filtering starred entries"
    FilterUnstarred -> "Filtering unstarred entries"
    FilterStarIgnored -> "Not filtered by star"
    FilterDone -> "Filtering done entries"
    FilterUndone -> "Filtering undone entries"
    FilterDoneIgnored -> "Not filtered by done"
    FilterLink -> "Toggle to apply the filters to project navigation"
    FilterLinked -> "Filters applied to project navigation"
    ColorPickerDialogTitle -> "vLabeler - Color Picker"
    QuickLaunchManagerDialogTitle -> "Plugin Slots"
    QuickLaunchManagerDialogDescription ->
        "You can set frequently used batch edit plugins with their parameters to plugin slots " +
            "for quick access. The parameters stored in the plugin slots are independent of each other, " +
            "and do not affect the parameters saved when used normally."
    QuickLaunchManagerDialogHeaderTitle -> "Slot"
    QuickLaunchManagerDialogHeaderPlugin -> "Plugin"
    QuickLaunchManagerDialogHeaderForceAskParams -> "Always ask for parameters"
    QuickLaunchManagerDialogItemTitle -> "Slot %d"
    QuickLaunchManagerDialogOpenKeymap -> "Open keymap"
    TrackingSettingsDialogTitle -> "Track App Usages"
    TrackingSettingsDialogDescription ->
        "You can enable/disable usage data tracking here, which collects anonymous usage events such as " +
            "`Launch App` and `Use Plugin`. The collected data is used to improve vLabeler, " +
            "and it doesn't include any specific information about your project, data or personal information. " +
            "Please kindly enable tracking if you think it's acceptable. " +
            "You can check more details by clicking the `Details` button."
    TrackingSettingsDialogFirstTimeAlert ->
        "We are showing you this dialog because you are using vLabeler with tracking for the first time. " +
            "You can open this dialog and change the tracking settings anytime in the menu " +
            "`Setting` -> `Track App Usages...`"
    TrackingSettingsDialogEnabled -> "Enabled"
    TrackingSettingsDialogTrackingIdLabel -> "Tracking ID:"
    ProjectSettingDialogTitle -> "Project Settings"
    ProjectSettingOutputFileLabel -> "Output file"
    ProjectSettingOutputFileHelperText ->
        "Target file for the `Export` action.\nIf it's not set, `Export Overwriting` is disabled."
    ProjectSettingOutputFileDisabledPlaceholder -> "Disabled by current labeler"
    ProjectSettingOutputFileSelectorDialogTitle -> "Choose output file"
    ProjectSettingAutoExportHelperText ->
        "Automatically export all subprojects to their output files when saving the project.\n" +
            "This option is only effective when the `Output file` is set properly,\n" +
            "or has been fixed by the labeler."
    ImportEntriesDialogTitle -> "Import Project"
    ImportEntriesDialogItemSummaryTitle -> "%d Entries"
    ImportEntriesDialogItemTargetLabel -> "Target"
    ImportEntriesDialogItemIncompatible -> "Incompatible with current project"
    ImportEntriesDialogReplaceContent -> "Replace current content"
    ImportEntriesDialogReplaceContentDisabledDescription ->
        "The current project doesn't support appending entries, " +
            "so all the current entries will be replaced by imported ones."
}
