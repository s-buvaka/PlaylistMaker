package com.marat.hvatit.playlistmaker2.creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.common.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.AudioPlayerRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryPref
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryPrefImpl
import com.marat.hvatit.playlistmaker2.data.dto.JsonParserImpl
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.MainInteractor
import com.marat.hvatit.playlistmaker2.domain.api.SettingsInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.impl.AudioPlayerInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.MainInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.SettingsInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.models.SaveStack
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigatorImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

object Creator {

    private const val KEY_CART = "cart"

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(PlaylistMakerApp.applicationContext()))
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideAudioPlayer(
        priviewUrl: String,
        callback: AudioPlayerCallback
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(AudioPlayerRepositoryImpl(priviewUrl, callback))
    }

    fun provideJsonParser(): JsonParserImpl {
        return JsonParserImpl(provideGson())
    }

    fun provideGlideHelper(): GlideHelper {
        return GlideHelperImpl()
    }

    fun provideSaveStack(size: Int): SaveStack<Track> {
        return SaveStack<Track>(size, provideHistoryTracks())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideHistoryTracks())
    }

    fun provideMainInteractor(): MainInteractor {
        return MainInteractorImpl(provideHistoryTracks())
    }

    fun provideIntentNavigator(context: Context): IntentNavigator {
        return IntentNavigatorImpl(context)
    }

    private fun provideHistoryTracks(): HistoryPref {
        return HistoryPrefImpl(
            PlaylistMakerApp.applicationContext(), provideSharedPref(),
            provideGson()
        )
    }

    private fun provideSharedPref(): SharedPreferences {
        return PlaylistMakerApp.applicationContext()
            .getSharedPreferences(KEY_CART, Context.MODE_PRIVATE)
    }

    private fun provideGson(): Gson {
        return Gson()
    }

}