package com.bro.financetools.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsTracker {
  fun track(event: String, params: Map<String, Any?> = emptyMap())
}

class FirebaseAnalyticsTracker(
  private val analytics: FirebaseAnalytics,
) : AnalyticsTracker {
  override fun track(event: String, params: Map<String, Any?>) {
    val b = Bundle()
    params.forEach { (k, v) ->
      when (v) {
        null -> Unit
        is String -> b.putString(k, v)
        is Int -> b.putInt(k, v)
        is Long -> b.putLong(k, v)
        is Double -> b.putDouble(k, v)
        is Float -> b.putFloat(k, v)
        is Boolean -> b.putBoolean(k, v)
        else -> b.putString(k, v.toString())
      }
    }
    analytics.logEvent(event, b)
  }
}
