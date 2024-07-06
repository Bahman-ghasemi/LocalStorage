package com.example.localstorage

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

object StorageHelper {

    object InternalStorage {

        fun saveBitmapToInternal(context: Context, fileName: String, bitmap: Bitmap): Boolean {
            context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE).use {
                try {
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it))
                        throw IOException("cannot save picture!")
                    return true
                } catch (e: Exception) {
                    throw IOException("cannot save picture!")
                }
            }
        }

        fun loadImagesToInternal(context: Context): Flow<List<FileInfo>> =
            flow {
                emit(context.filesDir.listFiles()?.filter {
                    it.isFile && it.canRead() && it.name.endsWith(".jpg")
                }?.map {
                    val bytes = it.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    FileInfo(it.name, bitmap)
                } ?: emptyList())
            }

        fun deleteImageToInternal(context: Context, fileName: String): Boolean {
            return try {
                if (!context.deleteFile(fileName))
                    throw IOException("Cannot Delete File!")
                true
            } catch (e: IOException) {
                throw IOException("Cannot Delete File!")
            }

        }
    }

    object ExternalStorage {

        fun saveBitmapToExternal(context: Context, fileName: String, bitmap: Bitmap): Boolean {
            val isSDK29AndUp = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            val mediaCollection =
                if (isSDK29AndUp) MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
            return try {
                context.contentResolver.insert(mediaCollection, contentValues)?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream))
                            throw IOException("couldn't save on external storage!")
                    }
                } ?: throw IOException("couldn't save on external storage!")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun loadImagesToExternal(context: Context): Flow<List<ExternalFileInfo>> =
            flow {
                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                else
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT
                )

                context.contentResolver.query(
                    collection,
                    projection,
                    null,
                    null,
                    "${MediaStore.Images.Media.DATE_ADDED} ASC"
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
                    val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)

                    val items = mutableListOf<ExternalFileInfo>()
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val width = cursor.getLong(widthColumn)
                        val height = cursor.getLong(heightColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        items.add(ExternalFileInfo(id, name, uri, width, height))
                    }

                    emit(items)
                }
            }

        fun deleteImageToExternal(
            context: Context,
            uri: Uri
        ): IntentSender? {
            return try {
                context.contentResolver.delete(uri, null, null)
                null
            } catch (e: SecurityException) {
                return when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        MediaStore.createDeleteRequest(
                            context.contentResolver,
                            listOf(uri)
                        ).intentSender
                    }

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        val recoverableSecurityException = e as? RecoverableSecurityException
                        recoverableSecurityException?.userAction?.actionIntent?.intentSender
                    }

                    else -> null
                }
            }
        }
    }
}