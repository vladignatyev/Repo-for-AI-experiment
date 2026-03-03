package com.bro.financetools.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "onboarding")

/**
 * Simple persistence for onboarding segmentation.
 *
 * Note: For production, inject this via DI. For this prototype we keep it small.
 */
object OnboardingStore {
  private val completedKey = booleanPreferencesKey("completed")
  private val goalKey = stringPreferencesKey("goal")
  private val horizonKey = stringPreferencesKey("horizon")
  private val riskKey = stringPreferencesKey("risk")

  private var appContext: Context? = null

  fun init(context: Context) {
    appContext = context.applicationContext
  }

  private fun ctx(): Context = requireNotNull(appContext) {
    "OnboardingStore.init(context) not called"
  }

  suspend fun isOnboardingCompleted(): Boolean {
    return ctx().dataStore.data.first()[completedKey] ?: false
  }

  suspend fun complete(goal: String, horizon: String, risk: String) {
    ctx().dataStore.edit { prefs ->
      prefs[goalKey] = goal
      prefs[horizonKey] = horizon
      prefs[riskKey] = risk
      prefs[completedKey] = true
    }
  }
}
