package com.marat.hvatit.playlistmaker2.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.domain.models.Track

class TrackListAdapter(
    private val tracklist: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {
    var saveTrackListener: SaveTrackListener? = null
    private val creator: Creator = Creator



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_cell, parent, false)
        return TrackViewHolder(view as ViewGroup,creator.provideGlideHelper())
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