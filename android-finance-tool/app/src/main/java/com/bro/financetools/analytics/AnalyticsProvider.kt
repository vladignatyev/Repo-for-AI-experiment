package com.bro.financetools.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsProvider {
  fun provide(context: Context): AnalyticsTracker {
    val a = FirebaseAnalytics.getInstance(context)
    return FirebaseAnalyticsTracker(a)
  }
}
