package com.example.storyapp.util

import android.content.Context
import android.widget.Toast

const val SHARED_PREF_NAME = "story_app_preferences"
const val TOKEN = "token"
const val NO_ERROR = ""
const val ERROR_EMAIL_EMPTY = "Email can't be empty"
const val ERROR_EMAIL_PATTERN = "Email is invalid"
const val ERROR_PASSWORD_EMPTY = "Password can't be empty"
const val ERROR_PASSWORD_CHARACTER = "Password must be at least 6 characters"
const val ERROR_NAME_EMPTY = "Name can't be empty"
const val ERROR_DESCRIPTION_EMPTY = "Description can't be empty"

fun showMessage(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}