package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.Results
import com.example.storyapp.data.StoryRepository
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        vm = LoginViewModel(storyRepository)
    }

    @Test
    fun `successfull login should return success with correct name and the error should be false`(){
        val expectedResponse = MutableLiveData<Results<LoginResponse>>()
        val mockResponse = MockData.mockSuccessLoginResponse()

        expectedResponse.value = Results.Success(mockResponse)

        `when`(storyRepository.login(anyString(), anyString())).thenReturn(expectedResponse)

        val actualResponse = vm.login(anyString(), anyString()).getOrAwaitValue()
        verify(storyRepository).login(anyString(), anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Success)
        Assert.assertEquals(mockResponse.result.name, (actualResponse as Results.Success).data.result.name)
    }

    @Test
    fun `failure login should return error with correct message`(){
        val expectedResponse = MutableLiveData<Results<LoginResponse>>()
        val mockResponse = MockData.mockFailureLoginResponse()

        expectedResponse.value = Results.Error("User not found")

        `when`(storyRepository.login(anyString(), anyString())).thenReturn(expectedResponse)

        val actualResponse = vm.login(anyString(), anyString()).getOrAwaitValue()
        verify(storyRepository).login(anyString(), anyString())

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Results.Error)
        Assert.assertEquals(mockResponse.message, (actualResponse as Results.Error).error)
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