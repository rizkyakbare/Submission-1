package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.QuotePagingSource
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.Story
import com.example.storyapp.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var vm: MainViewModel
    private val dummyStory = MockData.generateDummyStory()


    @Before
    fun setUp() {
        vm = MainViewModel(storyRepository)
    }


    @Test
    fun `when Get allStories Should Not Null and Return Success`() = runTest {
        val data: PagingData<Story> = QuotePagingSource.snapshot(dummyStory)
        val expectedStories = MutableLiveData<PagingData<Story>>()

        expectedStories.value = data
        `when`(storyRepository.getAllStory(anyString())).thenReturn(expectedStories)

        val actualStory: PagingData<Story> = vm.getStories(anyString()).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)

    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


