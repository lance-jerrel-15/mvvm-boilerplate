package com.mvvm.commons.view

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

data class NavigateEvent(
    val actionId: Int,
    val args: Bundle? = null,
    val navOptions: NavOptions? = null,
    val extras: Navigator.Extras? = null
)

data class DeepLinkEvent(
    val deepLink: Uri,
    val navOptions: NavOptions? = null,
    val extras: Navigator.Extras? = null
)
