package com.xlp.verixsettings.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.R
import com.xlp.verixsettings.R.string.not_support_device
import com.xlp.verixsettings.R.string.not_support_root
import com.xlp.verixsettings.activity.pages.AndroidPage
import com.xlp.verixsettings.activity.pages.AppInstallPage
import com.xlp.verixsettings.activity.pages.ChargePage
import com.xlp.verixsettings.activity.pages.EggsPages
import com.xlp.verixsettings.activity.pages.MainPage
import com.xlp.verixsettings.activity.pages.SettingsPage
import com.xlp.verixsettings.activity.pages.SystemUiPage
import com.xlp.verixsettings.activity.pages.VibratorPage
import com.xlp.verixsettings.utils.BackupUtils
import com.xlp.verixsettings.utils.CheckedModel.checked
import com.xlp.verixsettings.utils.ShellUtils
import kotlin.system.exitProcess


class MainActivity : MIUIActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if(!BuildConfig.DEBUG){
        checkLSPosed()
        checkedRoot()
        checkModel()
        }
        super.onCreate(savedInstanceState)
    }

    private fun checkModel(){
        val checkState = checked()
        if (!checkState){
            MIUIDialog(this) {
                setTitle(R.string.tips)
                setMessage(not_support_device)
                setCancelable(false)
                setRButton(R.string.done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    private fun checkedRoot(){
        val checkRootState = ShellUtils.checkRootPermission()
        if (!checkRootState){
            MIUIDialog(this) {
                setTitle(R.string.tips)
                setMessage(not_support_root)
                setCancelable(false)
                setRButton(R.string.done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("VerixSteiings_Config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this) {
                setTitle(R.string.tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    init {
        activity = this
        registerPage(AndroidPage::class.java)
        registerPage(AppInstallPage::class.java)
        registerPage(ChargePage::class.java)
        registerPage(MainPage::class.java)
        registerPage(SettingsPage::class.java)
        registerPage(SystemUiPage::class.java)
        registerPage(VibratorPage::class.java)
        registerPage(EggsPages::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode == RESULT_OK) {
            when (requestCode) {
                BackupUtils.CREATE_DOCUMENT_CODE -> {
                    BackupUtils.handleCreateDocument(activity, data.data)
                }

                BackupUtils.OPEN_DOCUMENT_CODE -> {
                    BackupUtils.handleReadDocument(activity, data.data)
                }

            }
        }
    }

}
