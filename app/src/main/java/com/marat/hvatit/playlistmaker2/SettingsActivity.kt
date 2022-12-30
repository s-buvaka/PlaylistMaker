package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val PREFERENCE_NAME = "PREFERENCE_NAME"
        const val PREFERENCE_VALUE = "value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<View>(R.id.back)
        val buttonSwitch = findViewById<Switch>(R.id.bswitch)
        val buttonShare = findViewById<LinearLayout>(R.id.lltwo)
        val buttonSupport = findViewById<LinearLayout>(R.id.llthree)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.llfour)

        buttonShare.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent(ActionFilter.SHARE)
            }
        })

        buttonSupport.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent(ActionFilter.SUPPORT)
            }
        })

        buttonUserAgreement.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent(ActionFilter.USERAGREEMENT)
            }
        })

        buttonBack.setOnClickListener {
            onBackPressed()
        }

        val sharedPreference =  getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        buttonSwitch.isChecked = sharedPreference.getBoolean(PREFERENCE_VALUE, false)
        buttonSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked : Boolean) {
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) {
                        editor.putBoolean(PREFERENCE_VALUE,isChecked)
                        editor.apply()
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                    else{
                        editor.putBoolean(PREFERENCE_VALUE,isChecked)
                        editor.apply()
                        AppCompatDelegate.MODE_NIGHT_NO
                    })
            }
        })


    }

    enum class ActionFilter{
        SHARE,
        SUPPORT,
        USERAGREEMENT
    }

    fun createIntent(action:ActionFilter) {
        val shareIntent: Intent
        when (action) {
            ActionFilter.SHARE -> {
                val textshare = this.getString(R.string.text_share)
                shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, textshare)
                    type = "text/plain"
                }
                startActivity(shareIntent)
            }
            ActionFilter.SUPPORT -> {
                val tittleSend = this.getString(R.string.tittle_Send)
                val textSend = this.getString(R.string.text_Send)
                shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, R.string.my_email)
                    putExtra(Intent.EXTRA_SUBJECT, tittleSend)
                    putExtra(Intent.EXTRA_TEXT, textSend)

                }
                startActivity(shareIntent)
            }
            ActionFilter.USERAGREEMENT -> {
                val textuseragreement = this.getString(R.string.text_useragreement)
                shareIntent = Intent(Intent.ACTION_VIEW,Uri.parse(textuseragreement))
                startActivity(shareIntent)
            }
        }
    }
}