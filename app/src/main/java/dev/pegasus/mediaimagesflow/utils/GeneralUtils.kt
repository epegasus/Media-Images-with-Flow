package dev.pegasus.mediaimagesflow.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

object GeneralUtils {

    const val TAG = "MyTag"

    fun getFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }

    fun getDate(millis: Long): String {
        if (millis == 0L) return "Jan 01, 1997"
        val lastModDate = Date(millis)
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(lastModDate)
    }
}