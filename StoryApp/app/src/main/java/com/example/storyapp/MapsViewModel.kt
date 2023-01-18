package com.example.storyapp

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel()  {
    fun getStory(token: String) = storyRepository.getMapStory(token)
}