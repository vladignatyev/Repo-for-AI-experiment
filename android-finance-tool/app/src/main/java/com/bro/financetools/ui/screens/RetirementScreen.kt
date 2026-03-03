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
import com.bro.financetools.ui.viewmodel.RetirementViewModel
import com.bro.financetools.ui.widgets.LabeledNumberField

@Composable
fun RetirementScreen() {
  val context = LocalContext.current
  val tracker = AnalyticsProvider.provide(context)
  val vm: RetirementViewModel = viewModel(factory = SimpleVmFactory { RetirementViewModel(tracker) })
  val state by vm.state.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(text = stringResource(R.string.retirement_title), style = MaterialTheme.typography.titleLarge)

    val sym = stringResource(R.string.currency_symbol)
    val perMo = stringResource(R.string.unit_per_month)

    if (state.explain) {
      Card {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = stringResource(R.string.explain_retirement), style = MaterialTheme.typography.bodyMedium)
          Text(text = stringResource(R.string.explain_swr), style = MaterialTheme.typography.bodySmall)
        }
      }
    }

    LabeledNumberField(R.string.retirement_monthly_spend, state.monthlySpend, vm::updateSpend, suffix = sym + perMo)
    val pctYr = stringResource(R.string.unit_percent_per_year)

    LabeledNumberField(R.string.retirement_swr, state.swrPct, vm::updateSWR, suffix = pctYr)
    LabeledNumberField(R.string.retirement_buffer, state.bufferPct, vm::updateBuffer, suffix = "%")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(onClick = vm::calculate) { Text(stringResource(R.string.action_calculate)) }
      OutlinedButton(onClick = vm::reset) { Text(stringResource(R.string.action_reset)) }
      OutlinedButton(onClick = vm::toggleExplain) {
        Text(stringResource(if (state.explain) R.string.action_hide_explanation else R.string.action_explain))
      }
    }

    Card {
      Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("${stringResource(R.string.retirement_result_target)}: ${fmtMoney(state.target)}")
        Text("${stringResource(R.string.retirement_result_target_buffered)}: ${fmtMoney(state.targetBuffered)}")
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
