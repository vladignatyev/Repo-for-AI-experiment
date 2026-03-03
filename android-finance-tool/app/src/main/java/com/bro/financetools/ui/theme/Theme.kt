package com.bro.financetools.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun BroFinanceTheme(content: @Composable () -> Unit) {
  // Keep dark scheme to match finance "night" feel. Can be expanded later.
  MaterialTheme(
    colorScheme = darkColorScheme(),
    content = content
  )
}
