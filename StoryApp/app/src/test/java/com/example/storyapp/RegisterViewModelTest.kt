package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Results
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.LoginResponse
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var vm: RegisterViewModel

    @Before
    fun setUp() {
        vm = RegisterViewModel(storyRepository)
    }

    @Test
    fun `successfull register should return success with correct message and the error should be false`(){
        val expectedResponse = MutableLiveData<Results<DefaultResponse>>()
        val mockResponse = MockData.mockDefaultResponse()

        expectedResponse.value = Results.Success(mockResponse)

        `when`(storyRepository.register(anyString(), anyString(), anyString())).thenReturn(expectedResponse)

        val actualResponse = vm.register(anyString(), anyString(), anyString()).getOrAwaitValue()
        verify(storyRepository).register(anyString(), anyString(), anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Success)
        Assert.assertEquals(mockResponse.message, (actualResponse as Results.Success).data.message)
    }

    @Test
    fun `failure register should return error with correct message`(){
        val expectedResponse = MutableLiveData<Results<DefaultResponse>>()
        val mockResponse = MockData.mockErrorDefaultResponse()

        expectedResponse.value = Results.Error("error")

        `when`(storyRepository.register(anyString(), anyString(), anyString())).thenReturn(expectedResponse)

        val actualResponse = vm.register(anyString(), anyString(), anyString()).getOrAwaitValue()
        verify(storyRepository).register(anyString(), anyString(), anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Error)
        Assert.assertEquals(mockResponse.message, (actualResponse as Results.Error).error)
    }

    @Test
    fun `valid name should return no error`() {
        val name = "test"
        val expected = NO_ERROR

        val actual = vm.validateName(name)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `empty name should return empty error message`() {
        val name = ""
        val expected = ERROR_NAME_EMPTY

        val actual = vm.validateName(name)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `valid email should return no error`() {
        val email = "example@mail.com"
        val expected = NO_ERROR

        val actual = vm.validateEmail(email)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `invalid email should return empty error message`() {
        val email = "example@mail"
        val expected = ERROR_EMAIL_PATTERN

        val actual = vm.validateEmail(email)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `empty email should return empty error message`() {
        val email = ""
        val expected = ERROR_EMAIL_EMPTY

        val actual = vm.validateEmail(email)
        Assert.assertEquals(expected, actual)
    }
}