package com.bro.financetools.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bro.financetools.analytics.AnalyticsTracker
import com.bro.financetools.domain.math.FinanceMath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SavingsState(
  val goalAmount: String = "50000",
  val alreadySaved: String = "5000",
  val years: String = "5",
  val annualReturnPct: String = "4.0",
  val monthlyNeeded: Double? = null,
  val explain: Boolean = true,
)

class SavingsViewModel(
  private val analytics: AnalyticsTracker,
) : ViewModel() {
  private val _state = MutableStateFlow(SavingsState())
  val state: StateFlow<SavingsState> = _state

  fun updateGoal(v: String) = _state.update { it.copy(goalAmount = v) }
  fun updateSaved(v: String) = _state.update { it.copy(alreadySaved = v) }
  fun updateYears(v: String) = _state.update { it.copy(years = v) }
  fun updateReturn(v: String) = _state.update { it.copy(annualReturnPct = v) }
  fun toggleExplain() = _state.update { it.copy(explain = !it.explain) }

  fun reset() {
    _state.value = SavingsState()
  }

  fun calculate() {
    val s = _state.value
    val goal = s.goalAmount.toDoubleOrNull() ?: 0.0
    val saved = s.alreadySaved.toDoubleOrNull() ?: 0.0
    val years = s.years.toDoubleOrNull() ?: 0.0
    val r = (s.annualReturnPct.toDoubleOrNull() ?: 0.0) / 100.0

    val monthly = FinanceMath.monthlyNeededForGoal(
      goalAmount = goal,
      alreadySaved = saved,
      annualRate = r,
      years = years,
    )

    analytics.track(
      "calc_savings_goal",
      mapOf("goal" to goal, "saved" to saved, "years" to years, "return" to r)
    )

    _state.update { it.copy(monthlyNeeded = monthly) }
  }
}
