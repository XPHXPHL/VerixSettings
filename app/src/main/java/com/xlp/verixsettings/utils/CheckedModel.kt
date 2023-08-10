package com.xlp.verixsettings.utils

import java.util.Locale

object CheckedModel {
    fun checked(): Boolean {
        val meizuModel = getProp("ro.product.vendor.brand")
        when (getProp("ro.product.vendor.name")) {
            "apollo" -> return true
            "munch" -> return true
        }
        when (meizuModel.lowercase(Locale.ROOT)) {
            "meizu" -> return true
        }
        return false
    }
}