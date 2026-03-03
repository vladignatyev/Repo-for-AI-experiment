package com.bro.financetools.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bro.financetools.analytics.AnalyticsTracker
import com.bro.financetools.data.OnboardingStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingState(
  val step: Int = 0,
  val goal: String? = null,
  val horizon: String? = null,
  val risk: String? = null,
  val isDone: Boolean = false,
)

class OnboardingViewModel(
  private val analytics: AnalyticsTracker,
) : ViewModel() {

  private val _state = MutableStateFlow(OnboardingState())
  val state: StateFlow<OnboardingState> = _state

  fun pickGoal(value: String) {
    _state.update { it.copy(goal = value) }
    analytics.track("onboarding_goal", mapOf("goal" to value))
  }

  fun pickHorizon(value: String) {
    _state.update { it.copy(horizon = value) }
    analytics.track("onboarding_horizon", mapOf("horizon" to value))
  }

  fun pickRisk(value: String) {
    _state.update { it.copy(risk = value) }
    analytics.track("onboarding_risk", mapOf("risk" to value))
  }

  fun next() {
    _state.update { it.copy(step = (it.step + 1).coerceAtMost(2)) }
  }

  fun back() {
    _state.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }
  }

  fun finish(skipSegmentation: Boolean = false) {
    analytics.track("onboarding_finish", mapOf("skip" to skipSegmentation))
    viewModelScope.launch {
      val s = _state.value
      val goal = s.goal ?: if (skipSegmentation) "skip" else "unknown"
      val horizon = s.horizon ?: if (skipSegmentation) "skip" else "unknown"
      val risk = s.risk ?: if (skipSegmentation) "skip" else "unknown"
      OnboardingStore.complete(goal, horizon, risk)
      _state.update { it.copy(isDone = true) }
    }
  }
}
