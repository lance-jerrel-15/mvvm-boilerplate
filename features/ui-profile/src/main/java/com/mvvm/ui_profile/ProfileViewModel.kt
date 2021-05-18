package com.mvvm.ui_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mvvm.commons.ControlledRunner
import com.mvvm.commons.base.IOViewModel
import com.mvvm.commons.util.result
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.commons.view.SingleLiveEvent
import com.mvvm.data.responses.LoginResponse
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.ReqresService
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProfileViewModel(private val reqresService: ReqresService) :
    IOViewModel<ProfileViewModelInputs, ProfileViewModelOutputs>(),
    ProfileViewModelInputs, ProfileViewModelOutputs {

    override val input: ProfileViewModelInputs = this
    override val output: ProfileViewModelOutputs = this

    private val _choiceEvent = SingleLiveEvent<Unit>()
    override var choiceEvent: LiveData<Result<UsersResponse>> = _choiceEvent.switchMap {
        getChoices()
    }

    init {
        viewModelScope.launch {
            _choiceEvent.call()
        }
    }

    fun choiceEvent() {
        _choiceEvent.call()
    }

    private fun getChoices(): LiveData<Result<UsersResponse>> =
        liveData(coroutineContextProvider.io) {
            emit(Result.Loading())
            try {
                val response = ControlledRunner<Response<UsersResponse>>().cancelPreviousThenRun {
                    reqresService.getUsers(1)
                }
                response.result(
                    onSuccess = {
                        emit(
                                Result.Success(response.body()!!)
                        )
                    },
                    onError = {
                    }
                )
            } catch (e: IOException) {
                emit(Result.Error<UsersResponse>(e.localizedMessage!!))
            }
        }
}