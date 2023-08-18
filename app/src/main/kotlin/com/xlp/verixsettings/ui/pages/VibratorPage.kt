package com.xlp.verixsettings.ui.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.Vibrator
import com.xlp.verixsettings.R.string.back_vibrator
import com.xlp.verixsettings.R.string.back_vibrator_summary
import com.xlp.verixsettings.R.string.face_vibrator
import com.xlp.verixsettings.R.string.face_vibrator_summary
import com.xlp.verixsettings.R.string.finger_vibrator
import com.xlp.verixsettings.R.string.finger_vibrator_summary

@BMPage("VibratorPage", hideMenu = false)
class VibratorPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(Vibrator))
        return getString(Vibrator)
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = back_vibrator, tipsId = back_vibrator_summary),
            SwitchV("back_vibrator")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = face_vibrator, tipsId = face_vibrator_summary),
            SwitchV("face_vibrator")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = finger_vibrator, tipsId = finger_vibrator_summary),
            SwitchV("finger_vibrator")
        )
    }
}