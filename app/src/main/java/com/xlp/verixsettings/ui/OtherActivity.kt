package com.xlp.verixsettings.ui

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import com.xlp.verixsettings.R
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class OtherActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Other)
        return PageFragment()
    }


     class PageFragment : BasePreferenceFragment(), Preference.OnPreferenceChangeListener {
        private var mHideIcon: Preference? = null
        override fun initPrefs() {
            mHideIcon = findPreference("hide_icon")
            mHideIcon?.onPreferenceChangeListener = this
        }

        override fun getContentResId(): Int {
            return R.xml.prefs_other
        }
        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
            val context = requireContext().applicationContext
            val pm = context.packageManager
            val componentName = ComponentName(context,"com.xlp.verixsettings.ui.MainActivity")
            if (preference === mHideIcon) {
                if (newValue as Boolean) {
                    pm.setComponentEnabledSetting(
                        componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                } else {
                    pm.setComponentEnabledSetting(
                        componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            return true
        }
    }
}