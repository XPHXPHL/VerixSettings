package com.xlp.verixsettings.utils

import java.util.HashMap
import java.util.LinkedHashSet

class PrefsMap : HashMap<String, Any>() {

    fun getObject(key: String, defValue: Any): Any {
        return getOrDefault(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return this[key] as? Int ?: defValue
    }

    fun getString(key: String, defValue: String): String {
        return this[key] as? String ?: defValue
    }

    fun getStringAsInt(key: String, defValue: Int): Int {
        return (this[key] as? String)?.toIntOrNull() ?: defValue
    }

    @Suppress("UNCHECKED_CAST")
    fun getStringSet(key: String): Set<String> {
        return this[key] as? Set<String> ?: LinkedHashSet()
    }

    fun getBoolean(key: String): Boolean {
        return this[key] as? Boolean ?: false
    }
}
