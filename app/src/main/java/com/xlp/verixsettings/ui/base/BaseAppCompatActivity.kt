package com.xlp.verixsettings.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xlp.verixsettings.R

abstract class BaseAppCompatActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.layoutDirection = resources.configuration.layoutDirection
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        supportFragmentManager.beginTransaction().replace(android.R.id.content, initFragment()).commit()
    }

    abstract fun initFragment(): Fragment

}
