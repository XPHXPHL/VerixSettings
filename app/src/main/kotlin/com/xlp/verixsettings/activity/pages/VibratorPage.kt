package com.xlp.verixsettings.activity.pages

import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity.Companion.safeSP
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.xlp.verixsettings.R
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
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.vibrator,
                tipsId = R.string.vibrator_summary,
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.vibrator)
                        setMessage(
                            "${activity.getString(R.string.def)}0，${activity.getString(R.string.current)}${
                                safeSP.getInt("vibrator", 0)
                            }"
                        )
                        setEditText("", "${activity.getString(R.string.range)}0-20")
                        setLButton(textId = R.string.cancel) {
                            dismiss()
                        }
                        setRButton(textId = R.string.done) {
                            try {
                                if (getEditText().toInt() in 0..20) {
                                    safeSP.putAny(
                                        "vibrator",
                                        getEditText().toInt()
                                    )
                                } else {
                                    safeSP.putAny(
                                        "vibrator",
                                        0
                                    )
                                    Toast.makeText(activity, R.string.warn, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } catch (e: NumberFormatException) {
                                Toast.makeText(activity, R.string.warn, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            dismiss()
                        }
                    }.show()
                })
        )
    }
}