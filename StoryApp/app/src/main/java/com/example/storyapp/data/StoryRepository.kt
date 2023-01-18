package com.example.storyapp.data

import androidx.lifecycle.LiveData
import com.example.storyapp.retrofit.ApiInterface
import okhttp3.MultipartBody
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.GetAllStoryResponse
import com.example.storyapp.model.LoginResponse
import com.example.storyapp.model.Story
import com.google.gson.Gson
import retrofit2.HttpException

class StoryRepository(
    private val apiInterface: ApiInterface
) {

    fun register(name: String, email: String, password: String): LiveData<Results<DefaultResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiInterface.register(name, email, password)
            emit(Results.Success(response))
        } catch (e: Exception) {
            if(e is HttpException) {
                val errorResponse = Gson().fromJson(e.response()?.errorBody()!!.charStream(), DefaultResponse::class.java)
                emit(Results.Error(errorResponse.message))
            } else {
                emit(Results.Error(e.message.toString()))
            }
        }
    }

    fun login(email: String, password: String): LiveData<Results<LoginResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiInterface.login(email, password)
            emit(Results.Success(response))
        } catch (e: Exception) {
            if(e is HttpException) {
                val errorResponse = Gson().fromJson(e.response()?.errorBody()!!.charStream(), DefaultResponse::class.java)
                emit(Results.Error(errorResponse.message))
            } else {
                emit(Results.Error(e.message.toString()))
            }
        }
    }

    fun getAllStory(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiInterface, token)
            }
        ).liveData
    }

    fun getMapStory(token: String): LiveData<Results<GetAllStoryResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiInterface.getStoriesWithLocation(token, 1, 5)
            emit(Results.Success(response))
        } catch (e: Exception) {
            if(e is HttpException) {
                val errorResponse = Gson().fromJson(e.response()?.errorBody()!!.charStream(), DefaultResponse::class.java)
                emit(Results.Error(errorResponse.message))
            } else {
                emit(Results.Error(e.message.toString()))
            }
        }
    }

    fun addStory(token: String, description: String, file: MultipartBody.Part): LiveData<Results<DefaultResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiInterface.addStory(token, description, file)
            emit(Results.Success(response))
        } catch (e: Exception) {
            if(e is HttpException) {
                val errorResponse = Gson().fromJson(e.response()?.errorBody()!!.charStream(), DefaultResponse::class.java)
                emit(Results.Error(errorResponse.message))
            } else {
                emit(Results.Error(e.message.toString()))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiInterface: ApiInterface,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiInterface)
            }.also { instance = it }
    }

}