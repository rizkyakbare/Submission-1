package com.example.storyapp

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.util.ERROR_DESCRIPTION_EMPTY
import com.example.storyapp.util.NO_ERROR
import okhttp3.MultipartBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun addStory(token: String, description: String, file: MultipartBody.Part) = storyRepository.addStory(token, description, file)

    fun validateDescription(description: String): String{
        return if(description.isEmpty()){
            ERROR_DESCRIPTION_EMPTY
        } else {
            NO_ERROR
        }
    }
}