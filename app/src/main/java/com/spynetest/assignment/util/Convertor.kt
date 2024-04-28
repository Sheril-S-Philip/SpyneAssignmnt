package com.spynetest.assignment.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Converters {
//    @TypeConverter
//    fun fromBitmap(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return outputStream.toByteArray()
//    }
//
//    @TypeConverter
//    fun toBitmap(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }

    fun createTempFileFromUri(contentResolver: ContentResolver, uriString: String): File? {
        val uri = Uri.parse(uriString)
        val inputStream = contentResolver.openInputStream(uri)

        return try {
            val tempFile = File.createTempFile("image-${System.currentTimeMillis()}", ".jpg")
            val outputStream = FileOutputStream(tempFile)

            inputStream?.use { input ->
                val bitmap = BitmapFactory.decodeStream(input)
                val rotatedBitmap = rotateBitmapIfRequired(contentResolver, bitmap, uri)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun rotateBitmapIfRequired(contentResolver: ContentResolver, bitmap: Bitmap, uri: Uri): Bitmap {
        val exif = ExifInterface(contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        val matrix = Matrix()
        if (rotationInDegrees != 0) {
            matrix.postRotate(rotationInDegrees.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }
}