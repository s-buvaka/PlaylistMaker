package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
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
    private val intentNavigator = Creator.provideIntentNavigator(this)

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

        buttonShare.setOnClickListener { viewModel.createIntent(ActionFilter.SHARE) }
        buttonSupport.setOnClickListener { viewModel.createIntent(ActionFilter.SUPPORT) }
        buttonUserAgreement.setOnClickListener { viewModel.createIntent(ActionFilter.USERAGREEMENT) }
        //.....................................................
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(interactor,intentNavigator)
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

}



