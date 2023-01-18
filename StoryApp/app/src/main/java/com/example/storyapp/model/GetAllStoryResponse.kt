package com.example.storyapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetAllStoryResponse(
    @SerializedName("listStory")
    val listStory: ArrayList<Story>
)

@Parcelize
data class Story(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("lon")
    val lon: Double,

    @SerializedName("lat")
    val lat: Double

): Parcelable
