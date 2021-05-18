package com.mvvm.ui_profile.register

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.mvvm.commons.base.IOViewModel
import com.mvvm.commons.util.updateEmail
import com.mvvm.commons.util.updateName

class RegisterViewModel() :
    IOViewModel<RegisterViewModelInputs, RegisterViewModelOutputs>(),
    RegisterViewModelInputs, RegisterViewModelOutputs {
    override val input: RegisterViewModelInputs = this
    override val output: RegisterViewModelOutputs = this

    override val name: MutableLiveData<String> = MutableLiveData("")
    override val email: MutableLiveData<String> = MutableLiveData("")
    override val bday: MutableLiveData<String> = MutableLiveData("")
    override val age: MutableLiveData<Long> = MutableLiveData(0L)
}