package com.marat.hvatit.playlistmaker2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}