package com.marat.hvatit.playlistmaker2.models

import android.os.Parcel
import android.os.Parcelable

data class Track (
     val trackName: String,
     val artistName: String,
     val trackTime: String,
     val artworkUrl100: String
) : Parcelable{
     constructor(parcel: Parcel) : this(
          parcel.readString().toString(),
          parcel.readString().toString(),
          parcel.readString().toString(),
          parcel.readString().toString()
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeString(trackName)
          parcel.writeString(artistName)
          parcel.writeString(trackTime)
          parcel.writeString(artworkUrl100)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<Track> {
          override fun createFromParcel(parcel: Parcel): Track {
               return Track(parcel)
          }

          override fun newArray(size: Int): Array<Track?> {
               return arrayOfNulls(size)
          }
     }
}