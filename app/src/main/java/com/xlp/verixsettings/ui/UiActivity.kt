package com.xlp.verixsettings.ui

import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class UiActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(R.string.Ui)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_ui
        }

        override fun initPrefs() {}
    }
}