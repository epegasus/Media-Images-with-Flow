package dev.pegasus.mediaimagesflow.viewModels

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.pegasus.mediaimagesflow.R
import dev.pegasus.mediaimagesflow.models.Picture
import dev.pegasus.mediaimagesflow.utils.GeneralUtils.TAG
import dev.pegasus.mediaimagesflow.utils.GeneralUtils.getDate
import dev.pegasus.mediaimagesflow.utils.GeneralUtils.getFileSize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val myFilesDirectory = application.resources.getString(R.string.app_name)
    private var functionName = "Nill"

    private var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "CoroutineExceptionHandler: $functionName", throwable)
    }

    private val _mediaImages = MutableStateFlow<List<Picture>>(emptyList())
    val mediaImages: StateFlow<List<Picture>> = _mediaImages

    fun fetchMediaImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUris = mutableListOf<Picture>()
            getMediaImages().collect { imageUri ->
                imageUris.add(imageUri)
                _mediaImages.value = imageUris
            }
        }
    }

    private suspend fun getMediaImages() = flow {
        functionName = "fetchPictures"

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE
        )
        val selection = getSelection(0)
        val selectionArgs = getDirectory(0)

        val orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        val cursor: Cursor? = getApplication<Application>().applicationContext.contentResolver.query(
            MediaStore.Images.Media.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            orderBy
        )

        cursor?.let {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataCol = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val addedCol = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val modifiedCol = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val sizeCol = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            if (it.moveToFirst()) {
                do {
                    val fileUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), it.getString(idCol).toLong())
                    val data = it.getString(dataCol)
                    val dateAdded = it.getLong(addedCol)
                    val dateModified = it.getLong(modifiedCol)
                    val size = it.getLong(sizeCol)
                    val file = File(data)
                    val picture = Picture(file, fileUri, file.name, getDate(dateAdded * 1000), getDate(dateModified * 1000), getFileSize(size), dateAdded, dateModified, size)
                    if (picture.file.exists()) {
                        emit(picture)
                    }
                } while (it.moveToNext())
            }
            it.close()
        }
    }

    private fun getSelection(caseType: Int): String? {
        return if (caseType == 0) {
            null
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?"
            } else {
                MediaStore.Images.Media.DATA + " LIKE ?"
            }
        }
    }

    /**
     * TODO
     * @param caseType         0: All Photos        1: Camera (DCIM)        2: Downloads        3: WhatsApp Images      4: Saved Images
     * @return
     */

    private fun getDirectory(caseType: Int): Array<String>? {
        return when (caseType) {
            0 -> null
            1 -> {
                arrayOf(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        "${Environment.DIRECTORY_DCIM}/%"
                    } else {
                        "%${Environment.DIRECTORY_DCIM}%"
                    }
                )
            }

            2 -> {
                arrayOf(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        "${Environment.DIRECTORY_DOWNLOADS}/%"
                    } else {
                        "%${Environment.DIRECTORY_DOWNLOADS}%"
                    }
                )
            }

            3 -> arrayOf("%WhatsApp Images%")
            4 -> arrayOf(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    "${Environment.DIRECTORY_PICTURES}/${myFilesDirectory}%"
                } else {
                    "%${Environment.DIRECTORY_PICTURES}/${myFilesDirectory}%"
                }
            )

            else -> null
        }
    }
}