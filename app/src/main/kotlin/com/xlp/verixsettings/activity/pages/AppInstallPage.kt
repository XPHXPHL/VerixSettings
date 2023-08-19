package com.xlp.verixsettings.activity.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.app_install
import com.xlp.verixsettings.R.string.silent_install
import com.xlp.verixsettings.R.string.silent_install_summary
import com.xlp.verixsettings.R.string.skip_virus_check_time
import com.xlp.verixsettings.R.string.skip_virus_check_time_summary

@BMPage("AppInstallPage", hideMenu = false)
class AppInstallPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(app_install))
        return (getString(app_install))
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = silent_install, tipsId = silent_install_summary),
            SwitchV("silent_install")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = skip_virus_check_time, tipsId = skip_virus_check_time_summary),
            SwitchV("skip_virus_check_time")
        )
    }
}