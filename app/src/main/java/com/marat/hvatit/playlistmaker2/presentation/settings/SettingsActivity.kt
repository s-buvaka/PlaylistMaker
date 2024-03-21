package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.R

private const val PREFERENCE_NAME = "PREFERENCE_NAME"
private const val PREFERENCE_VALUE = "value"
private const val TAG = "SettingsActivity"

enum class ActionFilter {
    SHARE,
    SUPPORT,
    USERAGREEMENT;
}

class SettingsActivity : AppCompatActivity() {
    private lateinit var  sharedPreferences : SharedPreferences
    private val gson: Gson = Gson()

    companion object {
        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SettingsActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = applicationContext.getSharedPreferences("cart", Context.MODE_PRIVATE)

        val buttonBack = findViewById<View>(R.id.back)
        val buttonSwitchTheme = findViewById<SwitchCompat>(R.id.bswitch)
        val buttonShare = findViewById<LinearLayout>(R.id.lltwo)
        val buttonSupport = findViewById<LinearLayout>(R.id.llthree)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.llfour)

        buttonShare.setOnClickListener { createIntent(ActionFilter.SHARE) }
        buttonSupport.setOnClickListener { createIntent(ActionFilter.SUPPORT) }
        buttonUserAgreement.setOnClickListener { createIntent(ActionFilter.USERAGREEMENT) }
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        buttonSwitchTheme.isChecked = isDarkMode()
        buttonSwitchTheme.setOnCheckedChangeListener { _, isChecked ->
            storeMode(isChecked)
            val mode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            saveThemeToCache(isChecked)

        }

    }

    private fun getPrefs(): SharedPreferences {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun isDarkMode(): Boolean {
        return getPrefs().getBoolean(PREFERENCE_VALUE, false)
    }

    private fun storeMode(isDark: Boolean) {
        getPrefs().edit().putBoolean(PREFERENCE_VALUE, isDark).apply()
    }


    private fun createIntent(action: ActionFilter) {
        val intentAction: Intent
        when (action) {
            ActionFilter.SHARE -> {
                val textshare = this.getString(R.string.text_share)
                intentAction = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, textshare)
                    type = "text/plain"
                }
                startActivity(intentAction)
            }
            ActionFilter.SUPPORT -> {
                val tittleSend = this.getString(R.string.tittle_Send)
                val textSend = this.getString(R.string.text_Send)
                intentAction = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, R.string.my_email)
                    putExtra(Intent.EXTRA_SUBJECT, tittleSend)
                    putExtra(Intent.EXTRA_TEXT, textSend)

                }
                startActivity(intentAction)
            }
            ActionFilter.USERAGREEMENT -> {
                val textuseragreement = this.getString(R.string.text_useragreement)
                intentAction = Intent(Intent.ACTION_VIEW, Uri.parse(textuseragreement))
                startActivity(intentAction)
            }
        }
    }

    private fun saveThemeToCache(isNightModeEnabled : Boolean) {
        sharedPreferences.edit()
            .putBoolean("night_mode_enabled",  isNightModeEnabled )
            .apply()
    }
}



