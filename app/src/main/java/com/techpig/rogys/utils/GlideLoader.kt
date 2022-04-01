package com.techpig.rogys.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.techpig.rogys.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_person_placeholder_1) // A default placeholder if image is failed to load.
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}