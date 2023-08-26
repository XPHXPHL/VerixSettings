package com.xlp.verixsettings.utils

import com.xlp.verixsettings.utils.Init.name

fun formatSocName(str: String):String{
    name = when (str){
        "SM_TOFINO" -> "骁龙7+ Gen2"
        "SM_PALMA" -> "骁龙8+ Gen1"
        "SM8350" -> "骁龙888"
        "SM8250" -> "骁龙865/骁龙870"
        "SM8150" -> "骁龙855"
        "SDM845" -> "骁龙845"
        "SDM710" -> "骁龙710"
        else -> str
    }
    return name
}