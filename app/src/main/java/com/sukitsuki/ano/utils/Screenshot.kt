package com.sukitsuki.ano.utils

import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Environment
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.sukitsuki.ano.AppConst.AppName
import com.sukitsuki.ano.R
import org.jetbrains.anko.doAsync
import permissions.dispatcher.PermissionRequest
import java.io.File
import java.io.FileOutputStream
import java.util.*


fun Context.takeScreenshot(bitmap: Bitmap): String? {
  val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppName)
  val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
  val mediaFile = File("${mediaStorageDir.path}${File.separator}$timestamp.jpg")
  return this.takeScreenshot(bitmap, mediaFile)
}

fun Context.takeScreenshot(bitmap: Bitmap, mediaFile: File): String? {
  if (mediaFile.isDirectory) return null
  if (!mediaFile.parentFile.exists() && !mediaFile.parentFile.mkdirs()) return null
  var compress = false
  doAsync {
    val fos = FileOutputStream(mediaFile)
    compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
    fos.close()
    SingleMediaScanner(this@takeScreenshot, mediaFile.parentFile)
  }.get()
  return if (compress) mediaFile.toString() else null
}

fun Context.showRationale(request: PermissionRequest, message: String) {
  AlertDialog.Builder(applicationContext).setMessage(R.string.alert_rationale_message)
    .setPositiveButton(R.string.yes) { _, _ -> request.proceed() }
    .setNegativeButton(R.string.no) { _, _ -> request.cancel() }
    .setCancelable(false).setMessage(message).show()!!
}


fun Context.showRationale(request: PermissionRequest, @StringRes message: Int): AlertDialog? {
  return AlertDialog.Builder(applicationContext).setMessage(R.string.alert_rationale_message)
    .setPositiveButton(R.string.yes) { _, _ -> request.proceed() }
    .setNegativeButton(R.string.no) { _, _ -> request.cancel() }
    .setCancelable(false).setMessage(message).show()
}
