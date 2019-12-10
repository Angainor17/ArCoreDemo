package com.simferopol.app.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

class CustomFileUtils {
    fun loadFile(context: Context, audioUrl: String?) {
        if (!audioUrl.isNullOrEmpty()) {
            var fileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1)
            var file =
                File(context?.getExternalFilesDir(null).toString() + "/downloads/" + fileName)
            if (!file.exists()) {
                var request = DownloadManager.Request(Uri.parse(audioUrl))
                    .setDestinationInExternalFilesDir(
                        context,
                        "downloads",
                        fileName
                    )
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                val downloadManager =
                    context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                var downloadID = downloadManager.enqueue(request)
            }
        }
    }
}