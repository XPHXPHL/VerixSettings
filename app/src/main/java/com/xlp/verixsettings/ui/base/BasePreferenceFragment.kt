package com.xlp.verixsettings.ui.base

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.xlp.verixsettings.utils.PrefsHelpers.mPrefsName

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {
    private var mContentResId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentResId = getContentResId()
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
        initPrefs()
    }

    abstract fun initPrefs()
    abstract fun getContentResId(): Int
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {}
}