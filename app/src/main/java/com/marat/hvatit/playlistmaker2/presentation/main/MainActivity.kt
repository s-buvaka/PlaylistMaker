package com.marat.hvatit.playlistmaker2.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.MedialibraryActivity
import com.marat.hvatit.playlistmaker2.presentation.search.SearchActivity
import com.marat.hvatit.playlistmaker2.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private val interactor = Creator.provideMainInteractor()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory(interactor)
        )[MainViewModel::class.java]

        setThemePref()
        initButtonSearch()
        initButtonMediaLib()
        initButtonSettings()
    }

    private fun initButtonSearch() {
        val buttonSearch = findViewById<Button>(R.id.button_bigOne)
        buttonSearch.setOnClickListener {
            SearchActivity.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
    }

    private fun initButtonSettings() {
        val buttonSettings = findViewById<Button>(R.id.button_bigThree)
        buttonSettings.setOnClickListener {
            SettingsActivity.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
    }

    private fun initButtonMediaLib() {
        findViewById<Button>(R.id.button_bigTwo).setOnClickListener {
            val medialibIntent = Intent(this, MedialibraryActivity::class.java)
            startActivity(medialibIntent)
        }
    }

    private fun setThemePref() {
        var flag: Boolean = viewModel.isDarkMode()
        val mode = if (flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
