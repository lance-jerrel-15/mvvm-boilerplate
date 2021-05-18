package com.mvvm.ui_doggo.dataSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mvvm.data.model.DoggoImageModel
import com.mvvm.data.services.DoggoService
import retrofit2.HttpException
import java.io.IOException

/**
 * provides the data source for paging lib from api calls
 */
@ExperimentalPagingApi
class DoggoImagePagingSource(val doggoService: DoggoService) :
    PagingSource<Int, DoggoImageModel>() {

    /**
     * calls api if there is any error getting results then return the [LoadResult.Error]
     * for successful response return the results using [LoadResult.Page] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [null] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DoggoImageModel> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = doggoService.getDoggoImages(page, params.loadSize)
            LoadResult.Page(
                response, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, DoggoImageModel>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }
}