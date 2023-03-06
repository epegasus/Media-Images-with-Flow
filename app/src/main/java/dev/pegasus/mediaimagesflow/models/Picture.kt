package dev.pegasus.mediaimagesflow.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

@Parcelize
data class Picture(
    var file: File,
    var fileUri: Uri,
    var fileName: String,
    var dateCreated: String,
    var dateModified: String,
    var fileSize: String,
    var dateCreatedMillis: Long,
    var dateModifiedMillis: Long,
    var fileSizeBytes: Long,
    var isSelected: Int = 0,
) : Parcelable