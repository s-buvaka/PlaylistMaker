package com.marat.hvatit.playlistmaker2.creator

import com.marat.hvatit.playlistmaker2.data.AudioPlayerRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.dto.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.dto.GsonHelperImpl
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.impl.AudioPlayerInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

object Creator {

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayer(priviewUrl: String, callback: AudioPlayerCallback): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(AudioPlayerRepositoryImpl(priviewUrl, callback))
    }

    fun provideJsonParser(): GsonHelperImpl {
        return GsonHelperImpl()
    }

    fun provideGlideHelper(): GlideHelper {
        return GlideHelperImpl()
    }


}