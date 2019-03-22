package com.sukitsuki.ano.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

class SingleMediaScanner(val context: Context, val file: File) :
  MediaScannerConnection.MediaScannerConnectionClient {
  private val mediaScannerConnection
    by lazy { MediaScannerConnection(context, this) }

  override fun onMediaScannerConnected() =
    mediaScannerConnection.scanFile(file.absolutePath, null)

  override fun onScanCompleted(path: String?, uri: Uri?) =
    mediaScannerConnection.disconnect()
}
