package com.xlp.verixsettings.ui

import androidx.fragment.app.Fragment
import androidx.preference.Preference
import com.xlp.verixsettings.R
import com.xlp.verixsettings.R.xml.prefs_ui
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment
import com.xlp.verixsettings.utils.execShell

class UiActivity : BaseAppCompatActivity(){

    override fun initFragment(): Fragment {
        setTitle(R.string.Ui)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment(), Preference.OnPreferenceChangeListener {
        private var mClockSeconds: Preference? = null
        override fun initPrefs() {
            mClockSeconds = findPreference("clock_seconds")
            mClockSeconds?.onPreferenceChangeListener = this
        }

        override fun getContentResId(): Int {
            return prefs_ui
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
            if (preference === mClockSeconds) {
                if (newValue as Boolean) {
                    execShell("settings put secure clock_seconds 1")
                } else {
                    execShell("settings put secure clock_seconds 0")
                }
            }
            return true
        }
    }
}