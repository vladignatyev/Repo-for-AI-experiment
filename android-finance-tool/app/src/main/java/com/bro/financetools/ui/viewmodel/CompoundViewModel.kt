package com.bro.financetools.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bro.financetools.analytics.AnalyticsTracker
import com.bro.financetools.domain.math.FinanceMath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CompoundState(
  val principal: String = "10000",
  val monthly: String = "500",
  val annualReturnPct: String = "7.0",
  val years: String = "20",
  val inflationPct: String = "0.0",
  val finalValue: Double? = null,
  val finalRealValue: Double? = null,
  val totalContrib: Double? = null,
  val gain: Double? = null,
  val explain: Boolean = true,
)

class CompoundViewModel(
  private val analytics: AnalyticsTracker,
) : ViewModel() {

  private val _state = MutableStateFlow(CompoundState())
  val state: StateFlow<CompoundState> = _state

  fun updatePrincipal(v: String) = _state.update { it.copy(principal = v) }
  fun updateMonthly(v: String) = _state.update { it.copy(monthly = v) }
  fun updateReturn(v: String) = _state.update { it.copy(annualReturnPct = v) }
  fun updateYears(v: String) = _state.update { it.copy(years = v) }
  fun updateInflation(v: String) = _state.update { it.copy(inflationPct = v) }
  fun toggleExplain() = _state.update { it.copy(explain = !it.explain) }

  fun reset() {
    _state.value = CompoundState()
  }

  fun calculate() {
    val s = _state.value
    val p = s.principal.toDoubleOrNull() ?: 0.0
    val m = s.monthly.toDoubleOrNull() ?: 0.0
    val r = (s.annualReturnPct.toDoubleOrNull() ?: 0.0) / 100.0
    val y = s.years.toDoubleOrNull() ?: 0.0
    val infl = (s.inflationPct.toDoubleOrNull() ?: 0.0) / 100.0

    val fv = FinanceMath.futureValueWithContributions(
      principal = p,
      monthlyContribution = m,
      annualRate = r,
      years = y,
      compoundsPerYear = 12,
    )

    val contrib = m * 12.0 * y
    val gain = fv - (p + contrib)
    val real = if (infl > 0 && y > 0) fv / Math.pow(1 + infl, y) else null

    analytics.track(
      "calc_compound",
      mapOf("years" to y, "return" to r, "principal" to p, "monthly" to m)
    )

    _state.update {
      it.copy(
        finalValue = fv,
        finalRealValue = real,
        totalContrib = contrib,
        gain = gain,
      )
    }
  }
}
