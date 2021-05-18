package com.mvvm.ui_doggo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.mvvm.data.model.DoggoImageModel
import com.mvvm.data.responses.UsersResponse

interface DoggoViewModelInputs

interface DoggoViewModelOutputs {
    var doggoEvent: LiveData<PagingData<DoggoImageModel>>
}