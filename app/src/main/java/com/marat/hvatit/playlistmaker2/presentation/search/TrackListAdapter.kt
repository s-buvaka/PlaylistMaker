package com.marat.hvatit.playlistmaker2.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter(
    private val tracklist: List<Track>
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {
    var saveTrackListener: SaveTrackListener? = null

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(roundedCornersImage))
                .into(trackImage)

        }

        private fun dateFormat(trackTime: String): String {
            //SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMills.toLong())
            return simpleDateFormat.format(trackTime.toLong())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_cell, parent, false)
        return TrackViewHolder(view as ViewGroup)
    }

    override fun getItemCount() = tracklist.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = tracklist[position]
        holder.bind(tracklist[position])
        holder.itemView.setOnClickListener {
            saveTrackListener?.addTrack(item)
        }
    }

    fun interface SaveTrackListener {
        fun addTrack(item: Track)

    }
}