package com.simferopol.app.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

fun loadFile(context: Context, audioUrl: String) {
    if (audioUrl.isNotEmpty()) {
        val fileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1)
        val file =
            File(context.filesDir.toString() + "/downloads/" + fileName)
        if (!file.exists()) {
            val request = DownloadManager.Request(Uri.parse(audioUrl))
                .setDestinationInExternalFilesDir(
                    context,
                    "downloads",
                    fileName
                )
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var downloadID = downloadManager.enqueue(request)
        }
    }
}
