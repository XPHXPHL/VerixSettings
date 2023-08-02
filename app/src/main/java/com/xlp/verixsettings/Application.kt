package com.xlp.verixsettings

import android.content.Context
import com.xlp.verixsettings.utils.PrefsUtils

class Application : android.app.Application() {

    override fun attachBaseContext(base: Context?) {
        PrefsUtils.mSharedPreferences = base?.let { PrefsUtils.getSharedPrefs(it, false) }
        super.attachBaseContext(base)
    }
}