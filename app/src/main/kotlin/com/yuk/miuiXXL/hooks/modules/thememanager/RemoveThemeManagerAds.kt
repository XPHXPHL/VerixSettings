package com.yuk.miuiXXL.hooks.modules.thememanager

import android.view.View
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.ConstructorFinder.`-Static`.constructorFinder
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.yuk.miuiXXL.hooks.modules.BaseHook
import com.yuk.miuiXXL.utils.getBoolean
import miui.drm.DrmManager

object RemoveThemeManagerAds : BaseHook() {
    override fun init() {

        if (!getBoolean("thememanager_remove_ads", false)) return
        try {
            DrmManager::class.java.methodFinder().filterByName("isSupportAd").toList().createHooks {
                before {
                    it.result = false
                }
            }
        } catch (t: Throwable) {
            Log.ex(t)
        }
        try {
            DrmManager::class.java.methodFinder().filterByName("setSupportAd").toList().createHooks {
                before {
                    it.result = false
                }
            }
        } catch (t: Throwable) {
            Log.ex(t)
        }
        try {
            loadClass("com.android.thememanager.basemodule.ad.model.AdInfoResponse").methodFinder().filterByName("isAdValid").filterByParamCount(1).first()
                .createHook {
                    before {
                        it.result = false
                    }
                }
        } catch (t: Throwable) {
            Log.ex(t)
        }
        hook(loadClass("com.android.thememanager.recommend.view.listview.viewholder.PureAdBannerViewHolder"))
        hook(loadClass("com.android.thememanager.recommend.view.listview.viewholder.SelfFontItemAdViewHolder"))
        hook(loadClass("com.android.thememanager.recommend.view.listview.viewholder.SelfRingtoneItemAdViewHolder"))
    }

    private fun hook(clazz: Class<*>) {
        try {
            clazz.constructorFinder().filterByParamCount(2).first().createHook {
                after {
                    if (it.args[0] != null) {
                        val view = it.args[0] as View
                        val params = FrameLayout.LayoutParams(0, 0)
                        view.layoutParams = params
                        view.visibility = View.GONE
                    }
                }
            }
        } catch (t: Throwable) {
            Log.ex(t)
        }
    }

}
