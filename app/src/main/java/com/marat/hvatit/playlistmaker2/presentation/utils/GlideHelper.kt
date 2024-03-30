package com.marat.hvatit.playlistmaker2.presentation.utils

import android.content.Context
import android.widget.ImageView
import com.marat.hvatit.playlistmaker2.domain.models.Track

interface GlideHelper {

    fun setImage(
        applicationContext: Context,
        song: Track,
        roundedCornersImage: Int,
        actplayerCover: ImageView
    )

}