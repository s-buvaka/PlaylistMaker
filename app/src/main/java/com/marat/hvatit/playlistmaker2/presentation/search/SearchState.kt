package com.marat.hvatit.playlistmaker2.presentation.search

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.marat.hvatit.playlistmaker2.R

interface Performable {
    fun setState()
}

sealed class SearchState : Performable {
    //DISCONNECTED, NOTHINGTOSHOW, ALLFINE, STARTSTATE, CLEARSTATE, DOWNLOAD
    abstract val placeholder: ImageView
    abstract val buttonUpdate: ImageButton
    abstract val textError: TextView
    abstract val progressBar: ProgressBar

    override fun setState() {

    }

    class Disconnected(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar,
        val buttonClearHistory: ImageButton,
        val textHistory: TextView,
        val message: String
    ) : SearchState(

    ) {
        override fun setState() {
            placeholder.setImageResource(R.drawable.disconnect_problem)
            placeholder.isVisible = true
            buttonUpdate.isVisible = true
            textError.text = message
            textError.isVisible = true
            buttonClearHistory.isVisible = false
            textHistory.isVisible = false
            progressBar.isVisible = false
        }

    }

    class NothingToShow(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar,
        val buttonClearHistory: ImageButton,
        val textHistory: TextView,
        val message: String
    ) : SearchState() {
        override fun setState() {
            placeholder.setImageResource(R.drawable.nothing_problem)
            placeholder.isVisible = true
            textError.text = message
            textError.isVisible = true
            buttonClearHistory.isVisible = false
            textHistory.isVisible = false
            progressBar.isVisible = false
        }
    }

    class StartState(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar,
        val buttonClearHistory: ImageButton,
        val textHistory: TextView
    ) : SearchState() {
        override fun setState() {
            progressBar.isVisible = false
            buttonUpdate.isVisible = false
            placeholder.isVisible = false
            textError.isVisible = false
            buttonClearHistory.isVisible = true
            textHistory.isVisible = true

        }
    }

    class ClearState(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar,
        val buttonClearHistory: ImageButton,
        val textHistory: TextView
    ) : SearchState() {
        override fun setState() {
            progressBar.isVisible = false
            buttonClearHistory.isGone = true
            textHistory.isGone = true
            buttonUpdate.isVisible = false
            placeholder.isVisible = false
            textError.isVisible = false
        }
    }

    class AllFine(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar
    ) : SearchState() {
        override fun setState() {
            buttonUpdate.isVisible = false
            placeholder.isVisible = false
            textError.isVisible = false
            progressBar.isVisible = false
        }
    }

    class Download(
        override val placeholder: ImageView,
        override val buttonUpdate: ImageButton,
        override val textError: TextView,
        override val progressBar: ProgressBar,
        val buttonClearHistory: ImageButton,
        val textHistory: TextView
    ) : SearchState() {
        override fun setState() {
            progressBar.isVisible = true
            buttonClearHistory.isGone = true
            textHistory.isGone = true
            buttonUpdate.isVisible = false
            placeholder.isVisible = false
            textError.isVisible = false
        }
    }


}
