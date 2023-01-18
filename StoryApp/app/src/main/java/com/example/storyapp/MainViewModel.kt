package com.example.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.Story

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getStories(token: String): LiveData<PagingData<Story>> = storyRepository.getAllStory(token).cachedIn(viewModelScope)
}