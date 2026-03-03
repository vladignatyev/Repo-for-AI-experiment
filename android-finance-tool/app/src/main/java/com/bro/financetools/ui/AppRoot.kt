package com.bro.financetools.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bro.financetools.ui.screens.HomeScreen
import com.bro.financetools.ui.screens.OnboardingScreen
import com.bro.financetools.ui.screens.SplashScreen
import com.bro.financetools.ui.viewmodel.SplashViewModel

object Routes {
  const val Splash = "splash"
  const val Onboarding = "onboarding"
  const val Home = "home"
}

@Composable
fun AppRoot() {
  val nav = rememberNavController()
  val vm: SplashViewModel = viewModel()
  val state by vm.state.collectAsState()

  LaunchedEffect(state.nextRoute) {
    val route = state.nextRoute ?: return@LaunchedEffect
    nav.navigate(route) {
      popUpTo(Routes.Splash) { inclusive = true }
      launchSingleTop = true
    }
    vm.onNavigated()
  }

  NavHost(navController = nav, startDestination = Routes.Splash) {
    composable(Routes.Splash) { SplashScreen(state = state) }
    composable(Routes.Onboarding) { OnboardingScreen(onFinish = { nav.navigate(Routes.Home) { popUpTo(Routes.Onboarding) { inclusive = true } } }) }
    composable(Routes.Home) { HomeScreen() }
  }
}
