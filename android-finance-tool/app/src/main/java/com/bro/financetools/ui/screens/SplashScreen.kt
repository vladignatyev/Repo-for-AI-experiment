package com.bro.financetools.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bro.financetools.ui.viewmodel.SplashState

@Composable
fun SplashScreen(state: SplashState) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    if (state.isLoading) {
      CircularProgressIndicator()
    } else {
      Text(text = state.message)
    }
  }
}
