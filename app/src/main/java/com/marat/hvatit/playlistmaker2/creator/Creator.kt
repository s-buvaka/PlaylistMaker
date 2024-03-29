package com.marat.hvatit.playlistmaker2.creator

import com.marat.hvatit.playlistmaker2.data.AudioPlayerImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.dto.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.dto.GsonHelperImpl
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.GlideHelper
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.impl.AudioPlayerProvider
import com.marat.hvatit.playlistmaker2.domain.impl.GlideProvider
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl

object Creator {

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayer(priviewUrl: String, callback: AudioPlayerCallback): AudioPlayerProvider {
        return AudioPlayerProvider(AudioPlayerImpl(priviewUrl, callback))
    }

    fun provideJsonParser(): GsonHelperImpl {
        return GsonHelperImpl()
    }

    fun provideGlideHelper(): GlideProvider {
        return GlideProvider(GlideHelperImpl())
    }


}