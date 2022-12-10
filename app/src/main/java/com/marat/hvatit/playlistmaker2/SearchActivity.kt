package com.marat.hvatit.playlistmaker2

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {

    private var saveEditText: String? = "????"

    companion object {
        const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val editText = findViewById<EditText>(R.id.editText)
        val buttonClear: ImageButton = findViewById(R.id.buttonClear)
        if (savedInstanceState != null) {
            editText.setText(saveEditText)
        }
        //..............................................................
        val simpletextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //empty
                clearButtonVisibility(s).also { buttonClear.visibility = it }
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
                saveEditText = editText.text.toString()
            }

        }
        editText.addTextChangedListener(simpletextWatcher)

        buttonClear.setOnClickListener{
            editText.requestFocus()
            (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken, 0)
        }

        Log.e("Hashcode", SearchActivity.hashCode().toString())

    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDITTEXT_TEXT,saveEditText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)
        saveEditText = savedInstanceState.getString(EDITTEXT_TEXT)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            ImageButton.GONE
        } else {
            ImageButton.VISIBLE
        }
    }

}