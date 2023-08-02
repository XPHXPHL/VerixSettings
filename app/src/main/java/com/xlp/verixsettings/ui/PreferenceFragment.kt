package com.xlp.verixsettings.ui

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.xlp.verixsettings.R
import com.xlp.verixsettings.utils.PrefsHelpers.mPrefsName

var mContentResId = 0

open class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentResId = R.xml.prefs
        if (mContentResId != 0) {
            try {
                preferenceManager.sharedPreferencesName = mPrefsName
                preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
                preferenceManager.setStorageDeviceProtected()
                PreferenceManager.setDefaultValues(requireActivity(), mContentResId, false)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
            addPreferencesFromResource(mContentResId)
        }
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {}
}