package com.bro.financetools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bro.financetools.R
import com.bro.financetools.analytics.AnalyticsProvider
import com.bro.financetools.ui.viewmodel.CompoundViewModel
import com.bro.financetools.ui.widgets.LabeledNumberField

@Composable
fun CompoundScreen() {
  val context = LocalContext.current
  val tracker = AnalyticsProvider.provide(context)
  val vm: CompoundViewModel = viewModel(factory = SimpleVmFactory { CompoundViewModel(tracker) })
  val state by vm.state.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(text = stringResource(R.string.compound_title), style = MaterialTheme.typography.titleLarge)

    if (state.explain) {
      Card {
        Text(
          text = stringResource(R.string.explain_compound),
          modifier = Modifier.padding(12.dp),
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }

    val sym = stringResource(R.string.currency_symbol)
    val perMo = stringResource(R.string.unit_per_month)

    LabeledNumberField(R.string.compound_principal, state.principal, vm::updatePrincipal, suffix = sym)
    LabeledNumberField(R.string.compound_monthly, state.monthly, vm::updateMonthly, suffix = sym + perMo)
    val pctYr = stringResource(R.string.unit_percent_per_year)
    val yShort = stringResource(R.string.unit_year_short)

    LabeledNumberField(R.string.compound_return, state.annualReturnPct, vm::updateReturn, suffix = pctYr)
    LabeledNumberField(R.string.compound_years, state.years, vm::updateYears, suffix = yShort)
    LabeledNumberField(R.string.compound_inflation, state.inflationPct, vm::updateInflation, suffix = pctYr)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(onClick = vm::calculate) { Text(stringResource(R.string.action_calculate)) }
      OutlinedButton(onClick = vm::reset) { Text(stringResource(R.string.action_reset)) }
      OutlinedButton(onClick = vm::toggleExplain) {
        Text(stringResource(if (state.explain) R.string.action_hide_explanation else R.string.action_explain))
      }
    }

    Card {
      val fv = state.finalValue
      val contrib = state.totalContrib
      val gain = state.gain
      val real = state.finalRealValue

      Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("${stringResource(R.string.compound_result_final)}: ${fmtMoney(fv)}")
        Text("${stringResource(R.string.compound_result_contrib)}: ${fmtMoney(contrib)}")
        Text("${stringResource(R.string.compound_result_gain)}: ${fmtMoney(gain)}")
        Text("${stringResource(R.string.compound_result_real)}: ${fmtMoney(real)}")
      }
    }
  }
}

@Composable
private fun fmtMoney(v: Double?): String {
  if (v == null || !v.isFinite()) return "—"
  val sym = stringResource(R.string.currency_symbol)
  val rounded = if (kotlin.math.abs(v) >= 100) v.toLong().toString() else String.format("%.2f", v)
  return sym + rounded
}
