package com.bro.financetools.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bro.financetools.R
import com.bro.financetools.ui.widgets.AdBanner

private data class HomeTab(
  val route: String,
  val labelRes: Int,
)

private val tabs = listOf(
  HomeTab("compound", R.string.home_compound),
  HomeTab("savings", R.string.home_savings),
  HomeTab("retirement", R.string.home_retirement),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
  val nav = rememberNavController()
  val backStack by nav.currentBackStackEntryAsState()
  val current = backStack?.destination?.route ?: tabs.first().route

  Scaffold(
    topBar = { TopAppBar(title = { Text(stringResource(R.string.home_title)) }) },
    bottomBar = {
      Column {
        NavigationBar {
          tabs.forEach { tab ->
            NavigationBarItem(
              selected = current == tab.route,
              onClick = {
                nav.navigate(tab.route) {
                  popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                  launchSingleTop = true
                  restoreState = true
                }
              },
              label = { Text(stringResource(tab.labelRes)) },
              icon = {}
            )
          }
        }
        AdBanner()
      }
    }
  ) { padding ->
    NavHost(
      navController = nav,
      startDestination = tabs.first().route,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      composable("compound") { CompoundScreen() }
      composable("savings") { SavingsGoalScreen() }
      composable("retirement") { RetirementScreen() }
    }
  }
}
