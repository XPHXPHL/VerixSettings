package com.xlp.verixsettings.ui.pages

import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.xlp.verixsettings.R.string.android
import com.xlp.verixsettings.R.string.app_shade
import com.xlp.verixsettings.R.string.app_shade_summary
import com.xlp.verixsettings.R.string.blur_enabled
import com.xlp.verixsettings.R.string.blur_enabled_summary
import com.xlp.verixsettings.R.string.clipboard_editor
import com.xlp.verixsettings.R.string.clipboard_editor_summary
import com.xlp.verixsettings.R.string.clock_seconds
import com.xlp.verixsettings.R.string.clock_seconds_summary

@BMPage("SystemUiPage", hideMenu = false)
class SystemUiPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(android))
        return getString(android)
    }

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(textId = blur_enabled, tipsId = blur_enabled_summary),
            SwitchV("blur_enabled")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = clock_seconds, tipsId = clock_seconds_summary),
            SwitchV("clock_seconds")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = app_shade, tipsId = app_shade_summary),
            SwitchV("app_shade")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = clipboard_editor, tipsId = clipboard_editor_summary),
            SwitchV("clipboard_editor")
        )
    }
}
