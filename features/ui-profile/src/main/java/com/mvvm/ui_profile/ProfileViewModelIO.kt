package com.mvvm.ui_profile

import androidx.lifecycle.LiveData
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.data.responses.UsersResponse

interface ProfileViewModelInputs

interface ProfileViewModelOutputs {
    var choiceEvent: LiveData<Result<UsersResponse>>
}