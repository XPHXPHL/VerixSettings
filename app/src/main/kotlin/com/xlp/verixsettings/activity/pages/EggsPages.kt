package com.xlp.verixsettings.activity.pages

import android.content.Intent
import android.net.Uri
import cn.fkj233.ui.activity.MIUIActivity.Companion.context
import cn.fkj233.ui.activity.annotation.BMMenuPage
import cn.fkj233.ui.activity.data.BasePage
import com.xlp.verixsettings.BuildConfig
import com.xlp.verixsettings.R
import com.xlp.verixsettings.R.drawable.about_creamdraft
import com.xlp.verixsettings.R.drawable.about_ex3124
import com.xlp.verixsettings.R.drawable.about_xph
import com.xlp.verixsettings.R.drawable.about_yukonga
import com.xlp.verixsettings.R.string.about_module
import com.xlp.verixsettings.R.string.about_us
import com.xlp.verixsettings.R.string.battery_health
import com.xlp.verixsettings.R.string.eggs_tips
import com.xlp.verixsettings.R.string.kernel_version
import com.xlp.verixsettings.R.string.module_build_time
import com.xlp.verixsettings.R.string.module_version
import com.xlp.verixsettings.R.string.module_version_r
import com.xlp.verixsettings.R.string.phone_state
import com.xlp.verixsettings.R.string.soc_chip
import com.xlp.verixsettings.utils.Init.batteryDesign
import com.xlp.verixsettings.utils.Init.batteryFull
import com.xlp.verixsettings.utils.Init.cameraParameter
import com.xlp.verixsettings.utils.Init.formatBatteryHealth
import com.xlp.verixsettings.utils.Init.formatSocChip
import com.xlp.verixsettings.utils.Init.kernelVersion
import com.xlp.verixsettings.utils.Init.romVersion
import java.text.SimpleDateFormat
import java.util.Locale

@BMMenuPage
class EggsPages : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(eggs_tips))
        return getString(eggs_tips)
    }

    override fun onCreate() {
        TitleText(textId = about_us)
        ImageWithText(
            this.getDrawable(about_xph),
            authorName = "XiaoPoHai(XPH)",
            onClickListener = {
                val url = "https://github.com/XPHXPHL"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        ImageWithText(
            this.getDrawable(about_yukonga),
            authorName = "YuKongA",
            onClickListener = {
                val url = "https://github.com/YuKongA"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        ImageWithText(
            this.getDrawable(about_ex3124),
            authorName = "Ex3124",
            onClickListener = {
                val url = "https://github.com/Ex3124"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        ImageWithText(
            this.getDrawable(about_creamdraft),
            authorName = "CreamDraft",
            onClickListener = {
                val url = "https://github.com/Cheng171"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        Line()
        TitleText(textId = about_module)
        TextSummary(
            textId = module_version,
            tips = "${BuildConfig.VERSION_NAME}-${BuildConfig.BUILD_TYPE}"
        )
        val buildTime =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(BuildConfig.BUILD_TIME)
        TextSummary(textId = module_build_time, tips = buildTime)
        TextSummary(
            textId = module_version_r, tips =
            "*B-代表测试版。\n" +
                    "*L-代表X.X版本的最后一版测试。\n" +
                    "*R-代表从X.X版本开始就有大改变(如Ui改变)但是这个版本号只会用1-3个版本然后就取消。\n" +
                    "*无后缀-正常在正常不过的版本"
        )
        Line()
        TitleText(textId = phone_state)
        TextSummary(
            textId = kernel_version, tips = kernelVersion
        )
        if (romVersion != "") TextSummary(
            textId = R.string.rom_version,
            tips = romVersion
        )
        if (cameraParameter != "") TextSummary(
            textId = R.string.camera_parameter,
            tips = cameraParameter
        )
        TextSummary(
            textId = soc_chip, tips = formatSocChip
        )
        TextSummary(
            textId = R.string.battery_design, tips = batteryDesign + "mAh(typ)"
        )
        TextSummary(
            textId = R.string.battery_full, tips = batteryFull + "mAh(typ)"
        )
        TextSummary(
            textId = battery_health, tips = formatBatteryHealth
        )
    }
}