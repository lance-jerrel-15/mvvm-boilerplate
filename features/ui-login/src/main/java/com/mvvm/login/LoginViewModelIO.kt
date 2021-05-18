package com.mvvm.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mvvm.commons.view.Event
import com.mvvm.data.responses.LoginResponse
import com.mvvm.commons.view.Result

interface LoginViewModelInputs {
    val email: MutableLiveData<String>
    val pass: MutableLiveData<String>
}

interface LoginViewModelOutputs {
    val loginEvent: LiveData<Event<Result<LoginResponse>>>
}
