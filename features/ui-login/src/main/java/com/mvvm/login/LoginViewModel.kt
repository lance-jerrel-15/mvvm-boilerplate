package com.mvvm.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mvvm.commons.ControlledRunner
import com.mvvm.commons.CoroutineContextProvider
import com.mvvm.commons.base.IOViewModel
import com.mvvm.commons.util.result
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.commons.view.SingleLiveEvent
import com.mvvm.data.responses.LoginResponse
import com.mvvm.data.services.ReqresService
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class LoginViewModel(private val reqresService: ReqresService,
                     override val coroutineContextProvider: CoroutineContextProvider) :
    IOViewModel<LoginViewModelInputs, LoginViewModelOutputs>(),
    LoginViewModelInputs, LoginViewModelOutputs {

    override val input: LoginViewModelInputs = this
    override val email: MutableLiveData<String> = MutableLiveData("")
    override val pass: MutableLiveData<String> = MutableLiveData("")

    override val output: LoginViewModelOutputs = this
    private val _loginEvent = SingleLiveEvent<Unit>()
    override val loginEvent: LiveData<Event<Result<LoginResponse>>> = _loginEvent.switchMap {
        loginUser()
    }

    fun login() = viewModelScope.launch {
        _loginEvent.call()
    }

    private fun loginUser(): LiveData<Event<Result<LoginResponse>>> =
        liveData(coroutineContextProvider.io) {
            emit(Event(Result.Loading()))
            try {
                val response = ControlledRunner<Response<LoginResponse>>().cancelPreviousThenRun {
                    reqresService.login(email = email.value ?: "", password = pass.value ?: "")
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
                                Result.Error<LoginResponse>(error ?: "")
                            )
                        )
                    }
                )
            } catch (e: IOException) {
                emit(Event(Result.Error<LoginResponse>(e.localizedMessage!!)))
            }
        }
}
