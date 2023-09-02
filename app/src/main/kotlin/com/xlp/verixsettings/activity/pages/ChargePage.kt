package com.xlp.verixsettings.activity.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.Charge
import com.xlp.verixsettings.R.string.battery_protect
import com.xlp.verixsettings.R.string.battery_protect_summary
import com.xlp.verixsettings.R.string.hs_power
import com.xlp.verixsettings.R.string.hs_power_summary
import com.xlp.verixsettings.utils.execShell

@BMPage("ChargePage", hideMenu = false)
class ChargePage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(Charge))
        return (getString(Charge))
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = battery_protect, tipsId = battery_protect_summary),
            SwitchV("battery_protect")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = hs_power, tipsId = hs_power_summary),
            SwitchV("hs_power", onClickListener = {
                if (it) {
                    execShell("echo 1 > /sys/class/qcom-battery/input_suspend")
                } else {
                    execShell("echo 0 > /sys/class/qcom-battery/input_suspend")
                }
            })
        )
    }
}
