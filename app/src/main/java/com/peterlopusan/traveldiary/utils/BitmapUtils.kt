package com.peterlopusan.traveldiary.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.peterlopusan.traveldiary.MainActivity
import java.io.InputStream

fun getBitmapFromUri(uri: Uri): Bitmap? {
    return try {
        val inputStream = MainActivity.instance.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getUriFromBitmap(image: Bitmap): Uri? {
    val path = MediaStore.Images.Media.insertImage(MainActivity.instance.contentResolver, image, "TravelDiary" +" " + getDate(), null)
    return Uri.parse(path)
}

fun getResizedBitmap(image: Bitmap): Bitmap {
    val maxSize = 1000
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()

    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }

    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun getRotatedBitmap(bitmap: Bitmap, inputStream: InputStream): Bitmap {
    val exitInterface = ExifInterface(inputStream)
    val rotatedBitmap = when (exitInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }
    return rotatedBitmap
}