package com.example.storyapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("loginResult")
    val result: LoginResult,
    
    @SerializedName("error")
    val error: Boolean,
    
    @SerializedName("message")
    val message: String
)

data class LoginResult(
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("token")
    val token: String
)
