package com.mvvm.ui_profile.register

import androidx.lifecycle.MutableLiveData

interface RegisterViewModelInputs {
    val name: MutableLiveData<String>
    val email: MutableLiveData<String>
    val bday: MutableLiveData<String>
    val age: MutableLiveData<Long>
}

interface RegisterViewModelOutputs {}
