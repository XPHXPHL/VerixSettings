package com.xlp.verixsettings.ui.pages

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.xlp.verixsettings.R
import com.xlp.verixsettings.R.drawable.ic_android
import com.xlp.verixsettings.R.drawable.ic_app_install
import com.xlp.verixsettings.R.drawable.ic_battery
import com.xlp.verixsettings.R.drawable.ic_settings
import com.xlp.verixsettings.R.drawable.ic_systemui
import com.xlp.verixsettings.R.drawable.ic_vibrator
import com.xlp.verixsettings.R.string.Charge
import com.xlp.verixsettings.R.string.Vibrator
import com.xlp.verixsettings.R.string.android
import com.xlp.verixsettings.R.string.app_install
import com.xlp.verixsettings.R.string.backup
import com.xlp.verixsettings.R.string.need_reboot
import com.xlp.verixsettings.R.string.reboot_system
import com.xlp.verixsettings.R.string.reboot_system_summary
import com.xlp.verixsettings.R.string.recovery
import com.xlp.verixsettings.R.string.restart_scope
import com.xlp.verixsettings.R.string.restart_scope_finished
import com.xlp.verixsettings.R.string.restart_scope_summary
import com.xlp.verixsettings.R.string.settings
import com.xlp.verixsettings.R.string.systemui
import com.xlp.verixsettings.R.string.tips
import com.xlp.verixsettings.ui.MainActivity
import com.xlp.verixsettings.utils.BackupUtils
import com.xlp.verixsettings.utils.execShell

@BMMainPage("Flyme VerixSettings")
class MainPage : BasePage() {
    @Suppress("DEPRECATION")
    @SuppressLint("WorldReadableFiles")
    override fun onCreate() {
        Page(
            this.getDrawable(ic_android),
            TextSummaryV(textId = android, tipsId = need_reboot),
            round = 8f,
            onClickListener = { showFragment("AndroidPage") })
        Line()
        Page(
            this.getDrawable(ic_systemui),
            TextSummaryV(textId = systemui),
            round = 8f,
            onClickListener = { showFragment("SystemUiPage") })

        Page(
            this.getDrawable(ic_settings),
            TextSummaryV(textId = settings),
            round = 8f,
            onClickListener = { showFragment("SettingsPage") })
        Page(
            this.getDrawable(ic_vibrator),
            TextSummaryV(textId = Vibrator),
            round = 8f,
            onClickListener = { showFragment("VibratorPage") })
        Page(
            this.getDrawable(ic_battery),
            TextSummaryV(textId = Charge),
            round = 8f,
            onClickListener = { showFragment("ChargePage") })
        Page(
            this.getDrawable(ic_app_install),
            TextSummaryV(textId = app_install),
            round = 8f,
            onClickListener = { showFragment("AppInstallPage") })
        Line()
        TextWithSwitch(
            TextV(textId = R.string.hide_desktop_icon),
            SwitchV("hide_desktop_icon", onClickListener = {
                val pm = MIUIActivity.activity.packageManager
                val mComponentEnabledState: Int = if (it) {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                }
                pm.setComponentEnabledSetting(
                    ComponentName(
                        MIUIActivity.activity,
                        MainActivity::class.java.name + "Alias"
                    ), mComponentEnabledState, PackageManager.DONT_KILL_APP
                )
            })
        )
        TextWithArrow(TextV(textId = backup, onClickListener = {
            BackupUtils.backup(
                activity,
                activity.createDeviceProtectedStorageContext()
                    .getSharedPreferences("VerixSteiings_Config", Context.MODE_WORLD_READABLE)
            )
        }))
        TextWithArrow(TextV(textId = recovery, onClickListener = {
            BackupUtils.recovery(
                activity,
                activity.createDeviceProtectedStorageContext()
                    .getSharedPreferences("VerixSteiings_Config", Context.MODE_WORLD_READABLE)
            )
        }))
        TextWithArrow(TextV(textId = restart_scope) {
            MIUIDialog(activity) {
                setTitle(tips)
                setMessage(restart_scope_summary)
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.done) {
                    execShell("killall com.android.systemui")
                    execShell("killall com.flyme.systemuiex")
                    execShell("killall com.android.settings")
                    execShell("killall com.android.packageinstaller")
                    Toast.makeText(
                        activity,
                        getString(restart_scope_finished),
                        Toast.LENGTH_SHORT
                    ).show()
                    Thread.sleep(500)
                    dismiss()
                }
            }.show()
        })
        TextWithArrow(TextV(textId = reboot_system) {
            MIUIDialog(activity) {
                setTitle(tips)
                setMessage(reboot_system_summary)
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.done) {
                    execShell("reboot")
                }
            }.show()
        })
    }
}