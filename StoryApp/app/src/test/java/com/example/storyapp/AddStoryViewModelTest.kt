package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Results
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.LoginResponse
import com.example.storyapp.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val folder = TemporaryFolder()


    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var vm: AddStoryViewModel

    @Before
    fun setUp() {
        vm = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `test successful add story should not null, return success with correct message, and the error should be false`() {
        val expectedResponse = MutableLiveData<Results<DefaultResponse>>()
        val mockResponse = MockData.mockDefaultResponse()
        val mockFile = MockData.createMockFile(folder)
        val mockMultipart = mockFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData(
            "photo",
            mockFile.name,
            mockMultipart
        )

        expectedResponse.value = Results.Success(mockResponse)

        `when`(storyRepository.addStory("abc", "test", image)).thenReturn(expectedResponse)

        val actualResponse = vm.addStory("abc", "test", image).getOrAwaitValue()
        verify(storyRepository).addStory("abc", "test", image)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Success)
        Assert.assertEquals(mockResponse.message, (actualResponse as Results.Success).data.message)
    }

    @Test
    fun `test failure add story should return error with correct error message`(){
        val expectedResponse = MutableLiveData<Results<DefaultResponse>>()
        val mockResponse = MockData.mockErrorDefaultResponse()
        val mockFile = MockData.createMockFile(folder)
        val mockMultipart = mockFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData(
            "photo",
            mockFile.name,
            mockMultipart
        )

        expectedResponse.value = Results.Error("error")

        `when`(storyRepository.addStory("abc", "test", image)).thenReturn(expectedResponse)

        val actualResponse = vm.addStory("abc", "test", image).getOrAwaitValue()
        verify(storyRepository).addStory("abc", "test", image)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Error)
        Assert.assertEquals(mockResponse.message, (actualResponse as Results.Error).error)
    }

    @Test
    fun `valid description should return no error`() {
        val description = "test"
        val expected = NO_ERROR

        val actual = vm.validateDescription(description)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `empty description should return empty error message`() {
        val description = ""
        val expected = ERROR_DESCRIPTION_EMPTY

        val actual = vm.validateDescription(description)
        Assert.assertEquals(expected, actual)
    }
}