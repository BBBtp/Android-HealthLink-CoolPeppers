package com.CoolPeppers.android.util

import android.content.Context
import android.net.Uri
import org.apache.commons.io.FileUtils
import java.io.File

fun createFileFromUri(name: String, uri: Uri, context: Context): File? {
    return try {
        val stream = context.contentResolver.openInputStream(uri)
        val file =
            File.createTempFile(
                "${name}_${System.currentTimeMillis()}",
                ".png",
                context.cacheDir
            )
        FileUtils.copyInputStreamToFile(stream, file)  // Use this one import org.apache.commons.io.FileUtils
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}