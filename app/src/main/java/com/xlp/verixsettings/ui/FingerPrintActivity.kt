package com.xlp.verixsettings.ui

import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class FingerPrintActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Fingerprint)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_fingerprint
        }

        override fun initPrefs() {}
    }
}