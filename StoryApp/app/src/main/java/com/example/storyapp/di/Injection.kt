package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.retrofit.ApiClient

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiInterface = ApiClient().getService()
        return StoryRepository.getInstance(apiInterface)
    }
}