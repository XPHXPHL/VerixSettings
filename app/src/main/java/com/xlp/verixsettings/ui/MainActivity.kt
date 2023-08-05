package com.xlp.verixsettings.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.FileObserver
import android.util.Log
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import com.xlp.verixsettings.R
import com.xlp.verixsettings.provider.SharedPrefsProvider
import com.xlp.verixsettings.utils.AppManager
import com.xlp.verixsettings.utils.PrefsHelpers
import com.xlp.verixsettings.utils.PrefsUtils
import com.xlp.verixsettings.utils.execShell
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var fileObserver: FileObserver? = null
    private var mPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppManager.getInstance().addActivity(this)
        window.decorView.layoutDirection = resources.configuration.layoutDirection
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        supportFragmentManager.beginTransaction().replace(android.R.id.content, MainFragment()).commit()
        initData()
        checkLSPosed()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance().removeActivity(this)
    }

    class MainFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {
        private var mHsPower: Preference? = null
        override fun initPrefs() {
            mHsPower = findPreference("hs_power")
            mHsPower?.onPreferenceChangeListener = this
        }


        override fun getContentResId(): Int {
            return R.xml.prefs
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
    private fun checkLSPosed(){
        if (!isModuleActive()){
            Toast.makeText(this, "模块未激活请激活后使用哦！", Toast.LENGTH_SHORT).show()
            exitProcess(0)
        }
    }
    private fun isModuleActive():Boolean{
        return false
    }
}
