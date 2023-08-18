package com.xlp.verixsettings.utils

import java.util.Locale

object CheckedModel {
    fun checked(): Boolean {
        val meizuModel = getProp("ro.p" + "ro" + "du" + "ct.v" + "en" + "do" + "r.br" + "and")
        when (getProp("ro.p" + "ro" + "du" + "ct.v" + "end" + "or.na" + "me")) {
            //Sm8350
            "fu" + "x" + "i" -> return true
            //Sm8250
            "ap" + "ol" + "lo" -> return true
            "mu" + "nch" -> return true
            "al" + "io" + "th" -> return true
            "c" + "a" + "s" -> return true
            "c" + "m" + "i" -> return true
            "u" + "mi" -> return true
            //Sm8150
            "ce" + "ph" + "e" + "us" -> return true
            "r" + "a" + "p" + "h" + "a" + "e" + "l" -> return true
            //SDM845
            "per" + "se" + "us" -> return true
            "po" + "la" + "ri" + "s" -> return true
            //Other
            "pi" + "ca" + "ss" + "o" -> return true
            "ph" + "oe" + "ni" + "x" -> return true
        }
        when (meizuModel.lowercase(Locale.ROOT)) {
            "me" + "i" + "zu" -> return true
            "s" + "o" + "ny" -> return true
            "len" + "ovo" -> return true
            "One" + "Plus" -> return true
        }
        return false
    }
}