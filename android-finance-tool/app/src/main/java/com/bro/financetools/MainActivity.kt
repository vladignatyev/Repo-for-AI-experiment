package com.bro.financetools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.bro.financetools.data.OnboardingStore
import com.bro.financetools.ui.AppRoot
import com.bro.financetools.ui.theme.BroFinanceTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    OnboardingStore.init(applicationContext)

    setContent {
      BroFinanceTheme {
        AppRoot()
      }
    }
  }
}
