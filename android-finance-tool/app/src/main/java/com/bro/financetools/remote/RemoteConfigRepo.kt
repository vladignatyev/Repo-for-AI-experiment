package com.bro.financetools.remote

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigRepo {
  suspend fun fetchAndActivate(): String {
    return runCatching {
      val rc = Firebase.remoteConfig
      rc.setConfigSettingsAsync(
        remoteConfigSettings {
          minimumFetchIntervalInSeconds = 3600
          fetchTimeoutInSeconds = 8
        }
      )

      // Defaults (works even if backend not configured)
      rc.setDefaultsAsync(mapOf("splash_message" to "Ready"))

      rc.fetchAndActivate().await()
      rc.getString("splash_message").ifBlank { "Ready" }
    }.getOrElse {
      "Offline mode"
    }
  }
}
