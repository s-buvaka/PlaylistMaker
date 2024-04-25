package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator

private const val TAG = "SettingsActivity"


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private val interactor = Creator.provideSettingsInteractor()

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

        val buttonBack = findViewById<View>(R.id.back)
        val buttonSwitchTheme = findViewById<SwitchCompat>(R.id.bswitch)
        val buttonShare = findViewById<LinearLayout>(R.id.lltwo)
        val buttonSupport = findViewById<LinearLayout>(R.id.llthree)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.llfour)

        buttonShare.setOnClickListener { createIntent(ActionFilter.SHARE) }
        buttonSupport.setOnClickListener { createIntent(ActionFilter.SUPPORT) }
        buttonUserAgreement.setOnClickListener { createIntent(ActionFilter.USERAGREEMENT) }
        //.....................................................
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(interactor)
        )[SettingsViewModel::class.java]


        buttonBack.setOnClickListener {
            onBackPressed()
        }

        buttonSwitchTheme.isChecked = viewModel.isDarkMode()

        buttonSwitchTheme.setOnCheckedChangeListener { _, isChecked ->
            val mode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            viewModel.storeMode(isChecked)


        }

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

}



