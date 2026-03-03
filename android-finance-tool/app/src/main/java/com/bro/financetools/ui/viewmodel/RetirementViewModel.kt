package com.bro.financetools.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bro.financetools.analytics.AnalyticsTracker
import com.bro.financetools.domain.math.FinanceMath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class RetirementState(
  val monthlySpend: String = "3000",
  val swrPct: String = "4.0",
  val bufferPct: String = "15",
  val target: Double? = null,
  val targetBuffered: Double? = null,
  val explain: Boolean = true,
)

class RetirementViewModel(
  private val analytics: AnalyticsTracker,
) : ViewModel() {

  private val _state = MutableStateFlow(RetirementState())
  val state: StateFlow<RetirementState> = _state

  fun updateSpend(v: String) = _state.update { it.copy(monthlySpend = v) }
  fun updateSWR(v: String) = _state.update { it.copy(swrPct = v) }
  fun updateBuffer(v: String) = _state.update { it.copy(bufferPct = v) }
  fun toggleExplain() = _state.update { it.copy(explain = !it.explain) }

  fun reset() {
    _state.value = RetirementState()
  }

  fun calculate() {
    val s = _state.value
    val spendM = s.monthlySpend.toDoubleOrNull() ?: 0.0
    val swr = (s.swrPct.toDoubleOrNull() ?: 0.0) / 100.0
    val buffer = (s.bufferPct.toDoubleOrNull() ?: 0.0) / 100.0

    val annual = spendM * 12.0
    val base = FinanceMath.retirementTarget(annualSpending = annual, swr = swr)
    val buffered = base * (1.0 + buffer.coerceIn(0.0, 1.0))

    analytics.track(
      "calc_retirement",
      mapOf("monthly_spend" to spendM, "swr" to swr, "buffer" to buffer)
    )

    _state.update { it.copy(target = base, targetBuffered = buffered) }
  }
}
