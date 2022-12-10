package com.marat.hvatit.playlistmaker2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonShare: ImageButton = findViewById(R.id.button_share)
        val buttonSupport: ImageButton = findViewById(R.id.button_support)
        val buttonUserAgreement: ImageButton = findViewById(R.id.button_useragreement)


        buttonShare.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent("Share")
            }
        })

        buttonSupport.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent("Support")
            }
        })

        buttonUserAgreement.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createIntent("UserAgreement")
            }
        })

    }

    fun createIntent(string: String) {
        val shareIntent: Intent
        when (string) {
            "Share" -> {
                val message = "https://practicum.yandex.ru/android-developer/"
                shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(shareIntent)
            }
            "Support" -> {
                val tittleSend = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
                val textSend = "Спасибо разработчикам и разработчицам за крутое приложение!"
                shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("decardcain21@gmail.com"))
                shareIntent.putExtra(Intent.EXTRA_TITLE, tittleSend)
                shareIntent.putExtra(Intent.EXTRA_TEXT, textSend)
                startActivity(shareIntent)
            }
            "UserAgreement" -> {
                val adress = "https://yandex.ru/legal/practicum_offer/"
                shareIntent = Intent(Intent.ACTION_VIEW,Uri.parse(adress))
                startActivity(shareIntent)
            }
            else -> {
                Toast.makeText(this@SettingsActivity, "This button disable!", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }
}