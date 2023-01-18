package com.example.storyapp

import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.util.*

class LoginViewModel(
    private val storyRepository: StoryRepository
    ): ViewModel() {

    fun login(email: String, password: String) = storyRepository.login(email, password)

    fun validateEmail(email: String): String{
        return if(email.isEmpty()){
            ERROR_EMAIL_EMPTY
        } else if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            ERROR_EMAIL_PATTERN
        } else {
            NO_ERROR
        }
    }

}