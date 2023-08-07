package com.xlp.verixsettings.ui

import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class VibratorActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Vibrator)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_vibrator
        }

        override fun initPrefs() {}
    }
}