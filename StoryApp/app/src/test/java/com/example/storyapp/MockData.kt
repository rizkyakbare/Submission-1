package com.example.storyapp

import androidx.paging.PagingData
import com.example.storyapp.model.*
import org.junit.rules.TemporaryFolder
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object MockData {

    const val mockUsername = "testUsername"
    const val mockPassword = "testPassword"

    fun mockSuccessLoginResponse(): LoginResponse =
        LoginResponse(
            LoginResult(
                "exUserId",
                "user",
                "exampleToken123"
            ),
            error = false,
            message = ""
        )

    fun mockFailureLoginResponse(): LoginResponse =
        LoginResponse(
            LoginResult(
                "exUserId",
                "user",
                "exampleToken123"
            ),
            error = true,
            message = "User not found"
        )

    fun mockDefaultResponse(): DefaultResponse =
        DefaultResponse(
            error = false,
            message = "success"
        )

    fun mockErrorDefaultResponse(): DefaultResponse =
        DefaultResponse(
            error = true,
            message = "error"
        )

    fun generateDummyStory(): List<Story> {
        val storyList : MutableList<Story> = arrayListOf()
        for (i in 0..10) {
            val story = Story(
                "12315$i",
                "Story $i",
                "Description number $1",
                "https://story-api.dicoding.dev/images/stories/photos-1648721013029_xPquDWah.jpg",
                "2022-03-31T09:57:52.403Z",
                0.0,
                0.0
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyMapsStory(): GetAllStoryResponse{
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                "12315$i",
                "Story $i",
                "Description number $1",
                "https://story-api.dicoding.dev/images/stories/photos-1648721013029_xPquDWah.jpg",
                "2022-03-31T09:57:52.403Z",
                0.0,
                0.0
            )
            storyList.add(story)
        }
        return GetAllStoryResponse(
            listStory = storyList
        )
    }

    fun generateMockStory(): PagingData<Story> {
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                "12315$i",
                "Story $i",
                "Description number $1",
                "https://story-api.dicoding.dev/images/stories/photos-1648721013029_xPquDWah.jpg",
                "2022-03-31T09:57:52.403Z",
                0.0,
                0.0
            )
            storyList.add(story)
        }
        return PagingData.from(storyList)
    }

    fun createMockFile(folder: TemporaryFolder): File {
        val file = folder.newFile("demo.jpeg")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write("Demo content")
        bufferedWriter.close()
        return file
    }

}