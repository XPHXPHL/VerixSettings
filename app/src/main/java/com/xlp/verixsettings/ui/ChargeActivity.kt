package com.xlp.verixsettings.ui

import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class ChargeActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Charge)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_charge
        }

        override fun initPrefs() {}
    }
}