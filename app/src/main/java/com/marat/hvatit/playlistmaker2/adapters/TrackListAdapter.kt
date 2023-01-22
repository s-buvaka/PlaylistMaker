package com.marat.hvatit.playlistmaker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.models.Track

class TrackListAdapter(
        private val tracklist: List<Track>
        ): RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {
    class TrackViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView
        private val artistName: TextView
        private val trackTime: TextView
        //private val artworkUrl100: ImageView

        init {
            val itemView = LayoutInflater.from(itemView.context).inflate(R.layout.search_cell,itemView,false)
            trackName = itemView.findViewById(R.id.tvtrack_name)
            artistName = itemView.findViewById(R.id.tvartist_name)
            trackTime = itemView.findViewById(R.id.tv_songduration)

        }

        fun bind(model: Track) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = model.trackTime

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