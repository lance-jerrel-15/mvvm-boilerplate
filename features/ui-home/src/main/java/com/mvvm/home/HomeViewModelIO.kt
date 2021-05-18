package com.mvvm.home

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mvvm.data.responses.UsersResponse

interface HomeViewModelInputs

interface HomeViewModelOutputs {
    var usersEvent: LiveData<PagedList<UsersResponse.Data>>
}
