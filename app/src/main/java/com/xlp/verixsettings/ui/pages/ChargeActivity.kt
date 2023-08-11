package com.xlp.verixsettings.ui.pages

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import com.xlp.verixsettings.R
import com.xlp.verixsettings.R.xml.prefs_charge
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment
import com.xlp.verixsettings.utils.execShell

class ChargeActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Charge)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment(), Preference.OnPreferenceChangeListener {
        private var mHsPower: Preference? = null
        override fun initPrefs() {
            mHsPower = findPreference("hs_power")
            mHsPower?.onPreferenceChangeListener = this
        }

        override fun getContentResId(): Int {
            return prefs_charge
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
            if (preference === mHsPower) {
                if (newValue as Boolean) {
                    Toast.makeText(activity, "Enable HsPower", Toast.LENGTH_SHORT).show()
                    execShell("echo 0 > /sys/class/power_supply/battery/battery_charging_enabled")
                } else {
                    Toast.makeText(activity, "Disable HsPower", Toast.LENGTH_SHORT).show()
                    execShell("echo 1 > /sys/class/power_supply/battery/battery_charging_enabled")
                }
            }
            return true
        }
    }
}