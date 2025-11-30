package com.example.ecotec

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

object RealPathUtil {

    fun getRealPath(context: Context, uri: Uri): String? {

        val isKitKat = true

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // Documentos externos
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]

                if (type.equals("primary", true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }

            // Downloads
            else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    id.toLong()
                )
                return getDataColumn(context, contentUri, null, null)
            }

            // Media
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                val id = split[1]

                var contentUri: Uri? = null

                contentUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(id)

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }

        // MediaStore
        if ("content".equals(uri.scheme, true)) {
            return getDataColumn(context, uri, null, null)
        }

        // File directo
        if ("file".equals(uri.scheme, true)) {
            return uri.path
        }

        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        uri ?: return null

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri) =
        uri.authority == "com.android.externalstorage.documents"

    private fun isDownloadsDocument(uri: Uri) =
        uri.authority == "com.android.providers.downloads.documents"

    private fun isMediaDocument(uri: Uri) =
        uri.authority == "com.android.providers.media.documents"
}
