package com.xlp.verixsettings.ui.pages

import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R.string.Package
import com.xlp.verixsettings.R.xml.prefs_package
import com.xlp.verixsettings.ui.base.BaseAppCompatActivity
import com.xlp.verixsettings.ui.base.BasePreferenceFragment

class  PackageActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        setTitle(Package)
        return PageFragment()
    }

    class PageFragment : BasePreferenceFragment() {
        override fun getContentResId(): Int {
            return prefs_package
        }

        override fun initPrefs() {}
    }
}