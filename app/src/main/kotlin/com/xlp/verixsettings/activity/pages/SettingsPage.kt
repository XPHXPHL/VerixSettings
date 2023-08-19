package com.xlp.verixsettings.activity.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.cipher_disk_vibrator
import com.xlp.verixsettings.R.string.cipher_disk_vibrator_summary
import com.xlp.verixsettings.R.string.finger_unlock
import com.xlp.verixsettings.R.string.finger_unlock_summary
import com.xlp.verixsettings.R.string.settings

@BMPage("SettingsPage", hideMenu = false)
class SettingsPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(settings))
        return getString(settings)
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = cipher_disk_vibrator, tipsId = cipher_disk_vibrator_summary),
            SwitchV("cipher_disk_vibrator")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = finger_unlock, tipsId = finger_unlock_summary),
            SwitchV("finger_unlock")
        )
    }
}
