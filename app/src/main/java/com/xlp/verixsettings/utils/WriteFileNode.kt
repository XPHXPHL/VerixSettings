@file:Suppress("UNUSED_EXPRESSION")

package com.xlp.verixsettings.utils

import java.io.FileOutputStream
import java.io.IOException

class WriteFileNode(path: String, data: String) {
    init {
        var result: Boolean
        var fos: FileOutputStream? = null
         try {
            try {
                fos = FileOutputStream(path)
                fos.write(data.toByteArray(charset("US-ASCII")))
                result = true
                try {
                    fos.close()
                } catch (e: IOException) {
                }
            } catch (e2: IOException) {
                StringBuilder()
                e2.printStackTrace()
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e3: IOException) {
                    }
                }
                result = false
            }
            StringBuilder()
            result
        } catch (th: Throwable) {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e4: IOException) {
                }
            }
            throw th
        }
    }
}
