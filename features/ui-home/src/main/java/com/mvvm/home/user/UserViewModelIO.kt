package com.mvvm.home.user

import androidx.lifecycle.LiveData
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.data.responses.UserResponse

interface UserViewModelInputs

interface UserViewModelOutputs {
    var userEvent: LiveData<Event<Result<UserResponse>>>
}
