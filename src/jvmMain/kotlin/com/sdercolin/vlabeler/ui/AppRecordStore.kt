package com.sdercolin.vlabeler.ui

import com.sdercolin.vlabeler.env.Log
import com.sdercolin.vlabeler.model.AppRecord
import com.sdercolin.vlabeler.util.AppRecordFile
import com.sdercolin.vlabeler.util.stringifyJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppRecordStore(appRecord: AppRecord, private val scope: CoroutineScope) {

    private val _stateFlow = MutableStateFlow(appRecord)
    val stateFlow: StateFlow<AppRecord> = _stateFlow

    init {
        collectAndWrite()
    }

    private fun push(appRecord: AppRecord) {
        scope.launch(Dispatchers.IO) {
            _stateFlow.emit(appRecord)
        }
    }

    private fun collectAndWrite() {
        scope.launch(Dispatchers.IO) {
            _stateFlow.collectLatest {
                delay(ThrottlePeriodMs)
                AppRecordFile.writeText(it.stringifyJson())
                Log.info("Written appRecord: $it")
            }
        }
    }

    val value get() = stateFlow.value

    fun update(updater: AppRecord.() -> AppRecord) {
        push(updater(value))
    }

    companion object {
        private const val ThrottlePeriodMs = 500L
    }
}
