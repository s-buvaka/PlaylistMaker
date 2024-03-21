package com.marat.hvatit.playlistmaker2

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
)