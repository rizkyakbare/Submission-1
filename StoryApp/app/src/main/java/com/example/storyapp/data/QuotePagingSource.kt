package com.example.storyapp.data

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.model.Story
import com.example.storyapp.retrofit.ApiInterface
import retrofit2.HttpException
import java.io.IOException

class QuotePagingSource(private val apiInterface: ApiInterface, private val token: String): PagingSource<Int, Story>() {

    companion object {
        const val INITIAL_PAGE_INDEX = 1
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
//        return try {
//            val position = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = apiInterface.getStories(token, position, params.loadSize)
//
//            LoadResult.Page(
//                data = responseData.listStory,
//                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
//                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {

            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiInterface.getStories(token, position, params.loadSize)

            val data = responseData.listStory
            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException){
            return LoadResult.Error(exception)
        }
    }

}