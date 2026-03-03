package com.bro.financetools.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bro.financetools.data.OnboardingStore
import com.bro.financetools.remote.RemoteConfigRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashState(
  val isLoading: Boolean = true,
  val message: String = "Loading…",
  val nextRoute: String? = null,
)

class SplashViewModel(
  private val remote: RemoteConfigRepo = RemoteConfigRepo(),
) : ViewModel() {

  private val _state = MutableStateFlow(SplashState())
  val state: StateFlow<SplashState> = _state

  init {
    viewModelScope.launch {
      // Load Remote Config first (as requested). If it fails, proceed anyway.
      val msg = remote.fetchAndActivate()
      _state.update { it.copy(message = msg, isLoading = false) }

      val done = OnboardingStore.isOnboardingCompleted()
      _state.update { it.copy(nextRoute = if (done) "home" else "onboarding") }
    }
  }

  fun onNavigated() {
    _state.update { it.copy(nextRoute = null) }
  }
}
