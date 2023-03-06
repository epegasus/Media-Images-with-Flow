package dev.pegasus.mediaimagesflow.interfaces

import dev.pegasus.mediaimagesflow.models.Picture

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

interface OnPictureClickListener {
    fun onPictureClick(picture: Picture)
}