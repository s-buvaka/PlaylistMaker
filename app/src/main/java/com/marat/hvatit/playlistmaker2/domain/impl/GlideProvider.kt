package com.marat.hvatit.playlistmaker2.domain.impl

import android.content.Context
import android.widget.ImageView
import com.marat.hvatit.playlistmaker2.domain.api.GlideHelper
import com.marat.hvatit.playlistmaker2.domain.models.Track

class GlideProvider(private val glideHelper: GlideHelper) {

    fun actionWithGlide(
        applicationContext: Context,
        song: Track,
        roundedCornersImage: Int,
        actplayerCover: ImageView
    ) {
        glideHelper.setImage(
            applicationContext,
            song,
            roundedCornersImage,
            actplayerCover
        )
    }
}