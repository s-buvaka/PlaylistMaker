package com.marat.hvatit.playlistmaker2.presentation.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View, private val glide: GlideHelper) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val trackImage: ImageView
    private val roundedCornersImage: Int = 10
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    init {
        trackName = itemView.findViewById(R.id.tvtrack_name)
        artistName = itemView.findViewById(R.id.tvartist_name)
        trackTime = itemView.findViewById(R.id.tv_songduration)
        trackImage = itemView.findViewById(R.id.imageView)

    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = dateFormat(model.trackTimeMills)
        glide.setImage(itemView.context,model,roundedCornersImage,trackImage)
    }



    private fun dateFormat(trackTime: String): String {
        return simpleDateFormat.format(trackTime.toLong())
    }

}