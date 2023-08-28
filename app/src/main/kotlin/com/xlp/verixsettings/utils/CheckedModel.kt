package com.xlp.verixsettings.utils

import java.util.Locale

object CheckedModel {
    fun checked(): Boolean {
        val meizuModel = getProp("ro.product.vendor.brand")
        when (getProp("ro.product.vendor.name")) {
            //Sm8250
            "apollo" -> return true
            "munch" -> return true
            "alioth" -> return true
            "cas" -> return true
            "cmi" -> return true
            "umi" -> return true
            //Sm8150
            "cepheus" -> return true
            "raphael" -> return true
            //SDM845
            "perseus" -> return true
            "polaris" -> return true
            //Other
            "picasso" -> return true
            "phoenix" -> return true
            "marble" -> return true
        }
        when (meizuModel.lowercase(Locale.ROOT)) {
            "meizu" -> return true
            "sony" -> return true
            "lenovo" -> return true
        }
        return false
    }
}