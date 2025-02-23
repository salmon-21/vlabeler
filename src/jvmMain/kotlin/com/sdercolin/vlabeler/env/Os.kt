package com.sdercolin.vlabeler.env

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

/**
 * Information about the current operating system.
 */

val osName by lazy { System.getProperty("os.name") }
val osNameWithVersion by lazy { osName + " " + System.getProperty("os.version") }
val osInfo by lazy { osNameWithVersion + " " + System.getProperty("os.arch") }
val isWindows by lazy { osName.toLowerCase(Locale.current).contains("windows") }
val isMacOS by lazy { osName.toLowerCase(Locale.current).contains("mac") }
val isLinux by lazy { osName.toLowerCase(Locale.current).contains("linux") }
