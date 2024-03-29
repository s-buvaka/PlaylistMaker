package com.marat.hvatit.playlistmaker2.data.dto

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.GlideHelper
import com.marat.hvatit.playlistmaker2.domain.models.Track

class GlideHelperImpl : GlideHelper {

    override fun setImage(
        applicationContext: Context,
        song: Track,
        roundedCornersImage: Int,
        actplayerCover: ImageView
    ) {
        Glide.with(applicationContext)
            .load(song.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCornersImage))
            .into(actplayerCover)
    }
}