package com.example.storyapp.retrofit

import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.GetAllStoryResponse
import com.example.storyapp.model.LoginResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): DefaultResponse
    
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
    
    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ): DefaultResponse
    
    @GET("stories?location=1")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoryResponse
}