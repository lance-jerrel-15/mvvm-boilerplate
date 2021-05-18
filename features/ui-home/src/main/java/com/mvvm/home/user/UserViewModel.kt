package com.mvvm.home.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.mvvm.commons.ControlledRunner
import com.mvvm.commons.base.IOViewModel
import com.mvvm.commons.util.result
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.commons.view.SingleLiveEvent
import com.mvvm.data.responses.UserResponse
import com.mvvm.data.services.ReqresService
import retrofit2.Response
import java.io.IOException

class UserViewModel(private val reqresService: ReqresService)
    : IOViewModel<UserViewModelInputs, UserViewModelOutputs>(),
    UserViewModelInputs, UserViewModelOutputs {
    override val input: UserViewModelInputs = this

    override val output: UserViewModelOutputs = this
    private val _userEvent = SingleLiveEvent<Int>()

    fun getUserId(userId: Int) {
        _userEvent.value = userId
    }

    override var userEvent: LiveData<Event<Result<UserResponse>>> = _userEvent.switchMap {
        displayUser(userId = it)
    }

    private fun displayUser(userId: Int) : LiveData<Event<Result<UserResponse>>> =
        liveData(coroutineContextProvider.io) {
            emit(Event(Result.Loading()))
            try {
                val response = ControlledRunner<Response<UserResponse>>().cancelPreviousThenRun {
                    reqresService.getUser(userId)
                }
                response.result(
                    onSuccess = {
                        emit(
                            Event(
                                Result.Success(response.body()!!)
                            )
                        )
                    },
                    onError = {
                        emit(
                            Event(
                                Result.Error<UserResponse>(error ?: "")
                            )
                        )
                    }
                )
            } catch (e: IOException) {
                emit(Event(Result.Error<UserResponse>(e.localizedMessage!!)))
            }
        }
}