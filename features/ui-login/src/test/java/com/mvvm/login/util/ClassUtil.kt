package com.mvvm.login.util

import kotlin.reflect.KClass

inline fun <reified T> createJavaClass() = T::class.java

inline fun <reified T> createKClass(): KClass<*> = T::class