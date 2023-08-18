package com.xlp.verixsettings.utils

import com.xlp.verixsettings.BuildConfig
import de.robv.android.xposed.XSharedPreferences

fun prefs() = XSharedPreferences(BuildConfig.APPLICATION_ID, "VerixSteiings_Config")

fun getBoolean(key: String, defValue: Boolean): Boolean {
    if (prefs().hasFileChanged()) prefs().reload()
    return prefs().getBoolean(key, defValue)
}

fun getInt(key: String, defValue: Int): Int {
    if (prefs().hasFileChanged()) prefs().reload()
    return prefs().getInt(key, defValue)
}

fun getFloat(key: String, defValue: Float): Float {
    if (prefs().hasFileChanged()) prefs().reload()
    return prefs().getFloat(key, defValue)
}
