package com.bro.financetools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bro.financetools.R
import com.bro.financetools.analytics.AnalyticsProvider
import com.bro.financetools.ui.viewmodel.OnboardingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
  val context = LocalContext.current
  val tracker = AnalyticsProvider.provide(context)
  val vm: OnboardingViewModel = viewModel(factory = SimpleVmFactory { OnboardingViewModel(tracker) })
  val state by vm.state.collectAsState()

  LaunchedEffect(state.isDone) {
    if (state.isDone) onFinish()
  }

  Column(modifier = Modifier.fillMaxSize()) {
    TopAppBar(
      title = { Text(stringResource(R.string.onboarding_title)) },
      scrollBehavior = androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    )

    Column(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
        text = stringResource(R.string.onboarding_subtitle),
        style = MaterialTheme.typography.bodyMedium
      )

      when (state.step) {
        0 -> {
          StepCard(
            title = stringResource(R.string.onboarding_goal_title),
            options = listOf(
              stringResource(R.string.onboarding_goal_retire) to "retire",
              stringResource(R.string.onboarding_goal_save) to "save",
              stringResource(R.string.onboarding_goal_grow) to "grow",
            ),
            onPick = { vm.pickGoal(it); vm.next() }
          )
        }
        1 -> {
          StepCard(
            title = stringResource(R.string.onboarding_horizon_title),
            options = listOf(
              stringResource(R.string.onboarding_horizon_soon) to "0_2",
              stringResource(R.string.onboarding_horizon_mid) to "3_10",
              stringResource(R.string.onboarding_horizon_long) to "10_plus",
            ),
            onPick = { vm.pickHorizon(it); vm.next() }
          )
        }
        2 -> {
          StepCard(
            title = stringResource(R.string.onboarding_risk_title),
            options = listOf(
              stringResource(R.string.onboarding_risk_low) to "low",
              stringResource(R.string.onboarding_risk_med) to "med",
              stringResource(R.string.onboarding_risk_high) to "high",
            ),
            onPick = { vm.pickRisk(it) }
          )
          Button(onClick = { vm.finish(skipSegmentation = false) }) {
            Text(stringResource(R.string.action_continue))
          }
        }
      }

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (state.step > 0) {
          OutlinedButton(onClick = { vm.back() }, contentPadding = PaddingValues(12.dp)) {
            Text(stringResource(R.string.action_back))
          }
        }
        OutlinedButton(onClick = { vm.finish(skipSegmentation = true) }, contentPadding = PaddingValues(12.dp)) {
          Text(stringResource(R.string.action_see_inside))
        }
      }
    }
  }
}

@Composable
private fun StepCard(
  title: String,
  options: List<Pair<String, String>>,
  onPick: (String) -> Unit,
) {
  Card {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(text = title, style = MaterialTheme.typography.titleMedium)
      options.forEach { (label, value) ->
        Button(onClick = { onPick(value) }, contentPadding = PaddingValues(12.dp)) {
          Text(label)
        }
      }
    }
  }
}
