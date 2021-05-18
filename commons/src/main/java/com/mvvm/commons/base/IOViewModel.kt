package com.mvvm.commons.base

import androidx.lifecycle.ViewModel
import com.mvvm.commons.CoroutineContextProvider

abstract class IOViewModel<Inputs, Outputs> : ViewModel() {
    abstract val input: Inputs
    abstract val output: Outputs

    protected open val coroutineContextProvider : CoroutineContextProvider = CoroutineContextProvider()
}
