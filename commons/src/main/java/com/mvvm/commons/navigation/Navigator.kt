package com.mvvm.commons.navigation

import androidx.core.net.toUri
import androidx.navigation.NavOptions
import com.mvvm.commons.R

fun userDeepLink(itemId: Int) = "com.mvvmboilerplate://user/$itemId".toUri()

fun registerDeepLink() = "com.mvvmboilerplate://profile/register".toUri()

fun defaultNavAnimation(block: ((NavOptions.Builder) -> Unit)? = null) =
    NavOptions.Builder().apply {
        block?.invoke(this)
    }.setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
        .build()
