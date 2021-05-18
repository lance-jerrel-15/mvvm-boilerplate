package com.mvvm.boilerplate.di

import com.mvvm.commons.CoroutineContextProvider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val appModules = module {
    factory { provideCompositeDisposable() }
    factory { provideCoroutineContextProvider()}
}

private fun provideCompositeDisposable() = CompositeDisposable()

private fun provideCoroutineContextProvider() = CoroutineContextProvider()