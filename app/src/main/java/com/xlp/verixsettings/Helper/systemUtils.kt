package com.xlp.verixsettings.Helper

class systemUtils {
    fun Setprop(prop: String, state: String) {
        ShellHelper("setprop ${prop} ${state}")
    }

    fun getProp(mKey: String): String =
        Class.forName("android.os.SystemProperties").getMethod("get", String::class.java)
            .invoke(Class.forName("android.os.SystemProperties"), mKey).toString()
}