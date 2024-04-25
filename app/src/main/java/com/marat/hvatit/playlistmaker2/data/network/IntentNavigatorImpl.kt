package com.marat.hvatit.playlistmaker2.data.network

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.ActionFilter

class IntentNavigatorImpl(private val context: Context) : IntentNavigator {
    override fun createIntent(action: ActionFilter) {

        val intentAction: Intent
        when (action) {
            ActionFilter.SHARE -> {
                val textshare = context.getString(R.string.text_share)
                intentAction = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, textshare)
                    type = "text/plain"
                }
                context.startActivity(intentAction)
            }

            ActionFilter.SUPPORT -> {
                val tittleSend = context.getString(R.string.tittle_Send)
                val textSend = context.getString(R.string.text_Send)
                intentAction = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, R.string.my_email)
                    putExtra(Intent.EXTRA_SUBJECT, tittleSend)
                    putExtra(Intent.EXTRA_TEXT, textSend)

                }
                context.startActivity(intentAction)
            }

            ActionFilter.USERAGREEMENT -> {
                val textuseragreement = context.getString(R.string.text_useragreement)
                intentAction = Intent(Intent.ACTION_VIEW, Uri.parse(textuseragreement))
                context.startActivity(intentAction)
            }
        }
    }
}