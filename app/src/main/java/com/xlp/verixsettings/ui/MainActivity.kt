package com.xlp.verixsettings.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.FileObserver
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xlp.verixsettings.R
import com.xlp.verixsettings.provider.SharedPrefsProvider
import com.xlp.verixsettings.utils.AppManager
import com.xlp.verixsettings.utils.PrefsHelpers
import com.xlp.verixsettings.utils.PrefsUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    private var fileObserver: FileObserver? = null
    private var mPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppManager.getInstance().addActivity(this)
        window.decorView.layoutDirection = resources.configuration.layoutDirection
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        supportFragmentManager.beginTransaction().replace(android.R.id.content, PreferenceFragment()).commit()
        initData()
    }

    private fun initData() {
        mPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
            Log.i("prefs", "Changed: $s")
            val `val` = sharedPreferences.all[s]
            var path = ""
            when (`val`) {
                is String -> path = "string/"
                is Set<*> -> path = "stringset/"
                is Int -> path = "integer/"
                is Boolean -> path = "boolean/"
            }
            contentResolver.notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null)
            if (path != "") contentResolver.notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s), null)
        }
        PrefsUtils.mSharedPreferences?.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener)
        PrefsHelpers.fixPermissionsAsync(applicationContext)
        try {
            fileObserver = object : FileObserver(File(PrefsUtils.sharedPrefsPath.toString()), CLOSE_WRITE) {
                override fun onEvent(event: Int, path: String?) {
                    PrefsHelpers.fixPermissionsAsync(applicationContext)
                }
            }
            fileObserver?.startWatching()
        } catch (t: Throwable) {
            Log.e("prefs", "Failed to start FileObserver!")
        }
    }

}
