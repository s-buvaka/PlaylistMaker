package com.marat.hvatit.playlistmaker2.creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.common.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.AudioPlayerRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorageImpl
import com.marat.hvatit.playlistmaker2.data.dto.JsonParserImpl
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.MainInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SettingsInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.impl.AudioPlayerInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.api.interactors.impl.MainInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.api.interactors.impl.SettingsInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.api.interactors.impl.TrackInteractorImpl
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigatorImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

object Creator {

    private const val KEY_CART = "cart"

    // можно сделать синглтоном
    // разобраться с неймингом ибо два TrackRepository
    private fun getTrackRepository(): com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository {
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

    fun provideJsonParser(): JsonParser { // нужно предотавлять интерфейс
        return JsonParserImpl(provideGson())
    }

    fun provideGlideHelper(): GlideHelper {
        return GlideHelperImpl()
    }

    fun provideSaveStack(size: Int): com.marat.hvatit.playlistmaker2.domain.models.TrackRepository {
        return com.marat.hvatit.playlistmaker2.domain.models.TrackRepository(size, provideHistoryTracks())
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

    // опционально - сделать синглтоном
    private fun provideHistoryTracks(): HistoryStorage {
        return HistoryStorageImpl(
            PlaylistMakerApp.applicationContext(), provideSharedPref(),
            provideGson()
        )
    }

    // опционально - сделать синглтоном
    private fun provideSharedPref(): SharedPreferences {
        return PlaylistMakerApp.applicationContext()
            .getSharedPreferences(KEY_CART, Context.MODE_PRIVATE)
    }

    private fun provideGson(): Gson {
        return Gson()
    }
}
