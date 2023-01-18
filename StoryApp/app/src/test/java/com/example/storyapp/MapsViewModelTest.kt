package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.storyapp.data.Results
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.GetAllStoryResponse
import com.example.storyapp.model.LoginResponse
import com.example.storyapp.model.Story
import com.example.storyapp.util.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var vm: MapsViewModel
    private val dummyStory = MockData.generateDummyMapsStory()


    @Before
    fun setUp() {
        vm = MapsViewModel(storyRepository)
    }

    @Test
    fun `when get story should return Success and correct size`() {
        val expectedResponse = MutableLiveData<Results<GetAllStoryResponse>>()
        expectedResponse.value = Results.Success(dummyStory)

        `when`(storyRepository.getMapStory(anyString())).thenReturn(expectedResponse)
        val actualResponse = vm.getStory(anyString()).getOrAwaitValue()
        verify(storyRepository).getMapStory(anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Success)
        Assert.assertEquals(dummyStory.listStory.size, (actualResponse as Results.Success).data.listStory.size)
    }

    @Test
    fun `when get story error Should Return Error`() {
        val expectedResponse = MutableLiveData<Results<GetAllStoryResponse>>()
        expectedResponse.value = Results.Error("error")

        `when`(storyRepository.getMapStory(anyString())).thenReturn(expectedResponse)
        val actualResponse = vm.getStory(anyString()).getOrAwaitValue()
        verify(storyRepository).getMapStory(anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Error)
    }
}