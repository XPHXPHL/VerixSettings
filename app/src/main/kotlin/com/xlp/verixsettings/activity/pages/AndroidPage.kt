package com.xlp.verixsettings.activity.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.android
import com.xlp.verixsettings.R.string.forced_screen_capture
import com.xlp.verixsettings.R.string.forced_screen_capture_summary
import com.xlp.verixsettings.R.string.game_fps
import com.xlp.verixsettings.R.string.game_fps_summary

@BMPage("AndroidPage", hideMenu = false)
class AndroidPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(android))
        return (getString(android))
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = game_fps, tipsId = game_fps_summary),
            SwitchV("game_fps")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = forced_screen_capture, tipsId = forced_screen_capture_summary),
            SwitchV("forced_screen_capture")
        )
    }
}