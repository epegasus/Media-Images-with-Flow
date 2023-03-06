package dev.pegasus.mediaimagesflow.adapters

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import dev.pegasus.mediaimagesflow.R

/**
 * @Author: SOHAIB AHMED
 * @Date: 06,March,2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

@BindingAdapter("imagePath")
fun ShapeableImageView.setImageFromPath(imagePath: String) {
    Glide
        .with(this)
        .load(imagePath)
        .placeholder(R.drawable.bg_glide)
        .transition(DrawableTransitionOptions.withCrossFade(100))
        .into(this)
}