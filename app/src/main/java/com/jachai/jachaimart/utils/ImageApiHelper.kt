package com.jachai.jachaimart.utils

import android.graphics.Bitmap
import com.jachai.jachaimart.JachaiApplication
import com.orhanobut.logger.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageApiHelper {
    fun convertBitmapToFile(imageBitmap: Bitmap): File {
        val file = File(JachaiApplication.getAppContext().cacheDir, "image.jpg")
        val bos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos)
        val bitmapData = bos.toByteArray()
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            Logger.d(e.message)
            e.printStackTrace()
        }

        return file
    }
}