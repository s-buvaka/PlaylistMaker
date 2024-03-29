package com.marat.hvatit.playlistmaker2.domain.api

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