package com.mvvm.boilerplate.ui.mainnav

import androidx.lifecycle.ViewModel
import com.mvvm.commons.livedatabus.Event
import com.mvvm.commons.livedatabus.LiveDataBus

class MainViewModel: ViewModel() {

    fun logout() {
        LiveDataBus.publish(Event.UNAUTHORIZED)
    }
}