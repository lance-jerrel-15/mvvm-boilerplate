package com.mvvm.home.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mvvm.commons.CoroutineContextProvider
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.ReqresService

class UsersDataSourceFactory(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val networkService: ReqresService)
    : DataSource.Factory<Int, UsersResponse.Data>() {

    val usersDataSourceLiveData = MutableLiveData<UsersDataSource>()

    override fun create(): DataSource<Int, UsersResponse.Data> {
        val usersDataSource = UsersDataSource(networkService, coroutineContextProvider)
        usersDataSourceLiveData.postValue(usersDataSource)
        return usersDataSource
    }
}