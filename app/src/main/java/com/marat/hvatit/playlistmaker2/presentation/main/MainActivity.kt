package com.marat.hvatit.playlistmaker2.presentation.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.MedialibraryActivity
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.presentation.search.SearchActivity
import com.marat.hvatit.playlistmaker2.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences =
            applicationContext.getSharedPreferences("cart", Context.MODE_PRIVATE)
        setThemePref()

        val buttonSearch = findViewById<Button>(R.id.button_bigOne)
        buttonSearch.setOnClickListener {
            SearchActivity.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
        //................................................................................
        val buttonMedialib = findViewById<Button>(R.id.button_bigTwo)
        buttonMedialib.setOnClickListener {
            val medialibIntent = Intent(this, MedialibraryActivity::class.java)
            startActivity(medialibIntent)
        }
        //................................................................................
        val buttonSettings = findViewById<Button>(R.id.button_bigThree)
        buttonSettings.setOnClickListener {
            SettingsActivity.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
    }

    private fun setThemePref() {
        val flag = sharedPreferences.getBoolean("night_mode_enabled", false)
        val mode =
            if (flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}