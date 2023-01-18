package com.example.storyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.di.Injection

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddStoryViewModel::class.java))
            return AddStoryViewModel(storyRepository) as T
        else if(modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(storyRepository) as T
        else if(modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(storyRepository) as T
        else if(modelClass.isAssignableFrom(RegisterViewModel::class.java))
            return RegisterViewModel(storyRepository) as T
        else if(modelClass.isAssignableFrom(MapsViewModel::class.java))
            return MapsViewModel(storyRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}