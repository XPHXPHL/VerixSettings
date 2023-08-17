package com.yuk.miuiXXL.hooks.modules.thememanager

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.FieldFinder.`-Static`.fieldFinder
import com.yuk.miuiXXL.utils.callMethod
import com.yuk.miuiXXL.utils.getBoolean
import com.yuk.miuiXXL.utils.getObjectField
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.luckypray.dexkit.DexKitBridge
import miui.drm.DrmManager
import java.io.File
import java.lang.reflect.Method

class FuckValidateTheme2 : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getBoolean("thememanager_fuck_validate_theme", false)) return
        System.loadLibrary("dexkit")
        DexKitBridge.create(lpparam.appInfo.sourceDir)?.use { bridge ->

            val map = mapOf(
                "DrmResult" to setOf("theme", "ThemeManagerTag", "/system", "check rights isLegal:"),
                "LargeIcon" to setOf(
                    "apply failed", "/data/system/theme/large_icons/", "default_large_icon_product_id", "largeicons", "relativePackageList is empty"
                ),
            )

            val resultMap = bridge.batchFindMethodsUsingStrings {
                queryMap(map)
            }

            val drmResult = resultMap["DrmResult"]!!
            assert(drmResult.size == 1)
            val drmResultDescriptor = drmResult.first()
            val drmResultMethod: Method = drmResultDescriptor.getMethodInstance(lpparam.classLoader)
            drmResultMethod.createHook {
                after {
                    it.result = DrmManager.DrmResult.DRM_SUCCESS
                }
            }

            val largeIcon = resultMap["LargeIcon"]!!
            assert(largeIcon.size == 1)
            val largeIconDescriptor = largeIcon.first()
            val largeIconMethod: Method = largeIconDescriptor.getMethodInstance(lpparam.classLoader)
            largeIconMethod.createHook {
                before {
                    val resource = it.thisObject.javaClass.fieldFinder()
                        .filterByType(loadClass("com.android.thememanager.basemodule.resource.model.Resource", lpparam.classLoader)).first()
                    val productId = it.thisObject.getObjectField(resource.name)?.callMethod("getProductId").toString()
                    val strPath = "/storage/emulated/0/Android/data/com.android.thememanager/files/MIUI/theme/.data/rights/theme/${productId}-largeicons.mra"
                    val file = File(strPath)
                    val fileParent = file.parentFile!!
                    if (!fileParent.exists()) fileParent.mkdirs()
                    file.createNewFile()
                }
            }
        }
    }

}
