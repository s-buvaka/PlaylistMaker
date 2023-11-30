package com.marat.hvatit.playlistmaker2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.models.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListAdapter(
    private val tracklist: List<Track>
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {
    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView
        private val artistName: TextView
        private val trackTime: TextView
        private val trackImage: ImageView
        private val roundedCornersImage : Int = 10

        init {
            trackName = itemView.findViewById(R.id.tvtrack_name)
            artistName = itemView.findViewById(R.id.tvartist_name)
            trackTime = itemView.findViewById(R.id.tv_songduration)
            trackImage = itemView.findViewById(R.id.imageView)

        }

        fun bind(model: Track) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = model.trackTime
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(roundedCornersImage))
                .into(trackImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_cell, parent, false)
        return TrackViewHolder(view as ViewGroup)
    }

    override fun getItemCount() = tracklist.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracklist[position])
    }
}