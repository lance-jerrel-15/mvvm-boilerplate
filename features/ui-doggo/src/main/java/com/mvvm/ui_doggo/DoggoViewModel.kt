package com.mvvm.ui_doggo

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.mvvm.commons.base.IOViewModel
import com.mvvm.data.model.DoggoImageModel
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.DoggoService
import com.mvvm.data.services.ReqresService
import com.mvvm.ui_doggo.dataSource.DoggoImagePagingSource

@ExperimentalPagingApi
class DoggoViewModel(private val doggoService: DoggoService) : IOViewModel<DoggoViewModelInputs, DoggoViewModelOutputs>(),
    DoggoViewModelInputs, DoggoViewModelOutputs {
    override val input: DoggoViewModelInputs = this
    override val output: DoggoViewModelOutputs = this

    override lateinit var doggoEvent: LiveData<PagingData<DoggoImageModel>>
    private val pagingConfig: PagingConfig = getDefaultPageConfig()

    init {
       doggoEvent = Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoService)}
        ).liveData.cachedIn(viewModelScope)
    }

    private fun getDefaultPageConfig() : PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}