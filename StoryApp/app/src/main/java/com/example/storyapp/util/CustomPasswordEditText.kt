package com.example.storyapp.util

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class CustomPasswordEditText: TextInputEditText {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        imeOptions = EditorInfo.IME_ACTION_DONE
        hint = "Password"
        setBackgroundResource( R.drawable.background_edit_text)
        setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
        typeface = Typeface.SANS_SERIF
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty() && s.length < 6) {
                    error = ERROR_PASSWORD_CHARACTER
                }
            }

        })
    }

}