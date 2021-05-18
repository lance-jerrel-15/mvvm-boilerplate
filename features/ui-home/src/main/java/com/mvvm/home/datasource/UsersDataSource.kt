package com.mvvm.home.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mvvm.commons.CoroutineContextProvider
import com.mvvm.commons.State
import com.mvvm.commons.util.convert
import com.mvvm.data.responses.ErrorMessageResponse
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.ReqresService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

class UsersDataSource(
    private val networkService: ReqresService,
    private val coroutineContextProvider: CoroutineContextProvider
) : PageKeyedDataSource<Int, UsersResponse.Data>() {

    var state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UsersResponse.Data>) {
        updateState(State.LOADING)
        CoroutineScope(coroutineContextProvider.io).launch {
            try {
                val response = networkService.getUsers(1)
                val data = response.body()?.data ?: emptyList()
                callback.onResult(
                    data,
                    null,
                    2
                )
                updateState(State.DONE)

                response.errorBody()?.convert<ErrorMessageResponse>()?.let {
                    Log.e("Error", it.message)
                    updateState(State.ERROR)
                }
            } catch (e: IOException) {
                Log.e("Error", e.localizedMessage!!)
                updateState(State.ERROR)
            }

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UsersResponse.Data>) {
        updateState(State.LOADING)
        CoroutineScope(coroutineContextProvider.io).launch {
            try {
                val response = networkService.getUsers(params.key)
                val data = response.body()?.data ?: emptyList()
                callback.onResult(
                    data,
                    params.key + 1
                )
                updateState(State.DONE)

                response.errorBody()?.convert<ErrorMessageResponse>()?.let {
                    Log.e("Error", it.message)
                    updateState(State.ERROR)
                }
            } catch (e: IOException) {
                Log.e("Error", e.localizedMessage!!)
                updateState(State.ERROR)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UsersResponse.Data>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}
