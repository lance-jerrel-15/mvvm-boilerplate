package com.mvvm.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mvvm.commons.State
import com.mvvm.commons.base.IOViewModel
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.ReqresService
import com.mvvm.home.datasource.UsersDataSource
import com.mvvm.home.datasource.UsersDataSourceFactory

class HomeViewModel(private val reqresService: ReqresService,
                    private var usersDataSourceFactory: UsersDataSourceFactory)
    : IOViewModel<HomeViewModelInputs, HomeViewModelOutputs>(),
    HomeViewModelInputs, HomeViewModelOutputs {
    override val input: HomeViewModelInputs = this
    override val output: HomeViewModelOutputs = this

    override lateinit var usersEvent: LiveData<PagedList<UsersResponse.Data>>
    private val pageSize = 5

    init {
        usersDataSourceFactory = UsersDataSourceFactory(coroutineContextProvider, reqresService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        usersEvent = LivePagedListBuilder(usersDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        usersDataSourceFactory.usersDataSourceLiveData,
        UsersDataSource::state
    )

    fun invalidate() {
        usersDataSourceFactory.usersDataSourceLiveData.value?.invalidate()
    }

    fun listIsEmpty(): Boolean {
        return usersEvent.value?.isEmpty() ?: true
    }
}
