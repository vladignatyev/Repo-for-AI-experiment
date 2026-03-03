package com.bro.financetools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

    // Results stay pinned at the top and are never covered by the scrollable input area.
    ResultCard(
      primaryLabel = stringResource(R.string.compound_result_final),
      primaryValue = fmtMoney(state.finalValue),
      secondaryLines = listOf(
        stringResource(R.string.compound_result_contrib) to fmtMoney(state.totalContrib),
        stringResource(R.string.compound_result_gain) to fmtMoney(state.gain),
        stringResource(R.string.compound_result_real) to fmtMoney(state.finalRealValue),
      )
    )

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
    val pctYr = stringResource(R.string.unit_percent_per_year)
    val yShort = stringResource(R.string.unit_year_short)

    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        LabeledNumberField(
          labelRes = R.string.compound_principal,
          descriptionRes = R.string.compound_principal_desc,
          value = state.principal,
          onValueChange = vm::updatePrincipal,
          suffix = sym,
        )
      }
      item {
        LabeledNumberField(
          labelRes = R.string.compound_monthly,
          descriptionRes = R.string.compound_monthly_desc,
          value = state.monthly,
          onValueChange = vm::updateMonthly,
          suffix = sym + perMo,
        )
      }
      item {
        LabeledNumberField(
          labelRes = R.string.compound_return,
          descriptionRes = R.string.compound_return_desc,
          value = state.annualReturnPct,
          onValueChange = vm::updateReturn,
          suffix = pctYr,
        )
      }
      item {
        LabeledNumberField(
          labelRes = R.string.compound_years,
          descriptionRes = R.string.compound_years_desc,
          value = state.years,
          onValueChange = vm::updateYears,
          suffix = yShort,
        )
      }
      item {
        LabeledNumberField(
          labelRes = R.string.compound_inflation,
          descriptionRes = R.string.compound_inflation_desc,
          value = state.inflationPct,
          onValueChange = vm::updateInflation,
          suffix = pctYr,
        )
      }
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(onClick = vm::calculate) { Text(stringResource(R.string.action_calculate)) }
          OutlinedButton(onClick = vm::reset) { Text(stringResource(R.string.action_reset)) }
          OutlinedButton(onClick = vm::toggleExplain) {
            Text(stringResource(if (state.explain) R.string.action_hide_explanation else R.string.action_explain))
          }
        }
      }
    }
  }
}

@Composable
private fun ResultCard(
  primaryLabel: String,
  primaryValue: String,
  secondaryLines: List<Pair<String, String>>,
) {
  Card {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text(text = primaryLabel, style = MaterialTheme.typography.labelMedium)
      Text(text = primaryValue, style = MaterialTheme.typography.headlineMedium)
      secondaryLines.forEach { (t, v) ->
        Text(text = "$t: $v", style = MaterialTheme.typography.bodyMedium)
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
