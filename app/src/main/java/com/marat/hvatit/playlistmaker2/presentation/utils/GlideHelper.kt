package com.marat.hvatit.playlistmaker2.presentation.utils

import android.content.Context
import android.widget.ImageView

interface GlideHelper {

    // сделали более универсальной. Теперь она работет не только с треками
    fun setImage(
        context: Context,
        url: String,
        actplayerCover: ImageView,
        roundedCornersImage: Int = DEFAULT_CORNER_RADIUS,
    )

    companion object{

        const val DEFAULT_CORNER_RADIUS = 10
    }
}
