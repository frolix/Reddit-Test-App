package com.example.reddittestapp.util

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import java.io.File

fun String.saveToInternalStorage(context: Context) {

    val memeUrl: String = this

    val directory = File(Environment.DIRECTORY_PICTURES)

    if (!directory.exists()) {
        directory.mkdirs()
    }
    val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val downloadUri = Uri.parse(memeUrl)

    val request = DownloadManager.Request(downloadUri).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(memeUrl.substring(memeUrl.lastIndexOf("/") + 1))
            .setDescription("")
            .setDestinationInExternalPublicDir(
                directory.toString(),
                memeUrl.substring(memeUrl.lastIndexOf("/") + 1)
            )
    }


    val downloadId = downloadManager.enqueue(request)
    val query = DownloadManager.Query().setFilterById(downloadId)
    var downloading = true
    while (downloading) {
        val cursor: Cursor = downloadManager.query(query)
        cursor.moveToFirst()
        if (cursor.getInt(
                cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            ) == DownloadManager.STATUS_SUCCESSFUL
        ) {
            downloading = false
        }
        cursor.close()
    }
}
