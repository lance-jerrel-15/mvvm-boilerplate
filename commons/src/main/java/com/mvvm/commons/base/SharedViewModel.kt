package com.mvvm.commons.base

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.mvvm.commons.view.DeepLinkEvent
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.NavigateEvent
import com.mvvm.commons.view.SingleLiveEvent

class SharedViewModel : ViewModel() {

    var currentNavController: LiveData<NavController>? = null

    enum class AuthenticationState {
        UNAUTHENTICATED, // Initial state, the user needs to authenticate
        AUTHENTICATED, // The user has authenticated successfully
        INVALID_AUTHENTICATION // Authentication failed
    }

    private val _authenticationState =
        MutableLiveData(AuthenticationState.UNAUTHENTICATED)
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    private val _navigate = SingleLiveEvent<NavigateEvent>()
    val navigateEvent: LiveData<Event<NavigateEvent>> = _navigate.map {
        Event(it)
    }

    private val _deepLinkTrigger = SingleLiveEvent<DeepLinkEvent>()
    val deepLinkEvent: LiveData<Event<DeepLinkEvent>> = _deepLinkTrigger.map {
        Event(it)
    }

    fun navigate(
        actionId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        this.navigate(NavigateEvent(actionId, args, navOptions))
    }

    fun navigate(navigateEvent: NavigateEvent) {
        _navigate.value = navigateEvent
        navigateEvent.extras?.let {
            _navigate.value = null // make value to null to GC view references
        }
    }

    fun navigate(
        deepLink: Uri,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    ) {
        this.navigate(DeepLinkEvent(deepLink, navOptions, extras))
    }

    fun navigate(deepLinkEvent: DeepLinkEvent) {
        _deepLinkTrigger.value = deepLinkEvent
        deepLinkEvent.extras?.let {
            _deepLinkTrigger.value = null // make value to null to GC view references
        }
    }

    fun refuseAuthentication() {
        _authenticationState.value =
            AuthenticationState.UNAUTHENTICATED
    }

    fun setAuthenticationState(authenticationState: AuthenticationState) {
        _authenticationState.value = authenticationState
    }
}