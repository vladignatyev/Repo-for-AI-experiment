package com.bro.financetools

import android.app.Application
import com.google.android.gms.ads.MobileAds

class BroFinanceApp : Application() {
  override fun onCreate() {
    super.onCreate()
    // AdMob init. Safe even with test IDs.
    MobileAds.initialize(this) {}
  }
}
