package com.mvvm.boilerplate.di

import androidx.paging.ExperimentalPagingApi
import com.mvvm.home.datasource.UsersDataSourceFactory
import com.mvvm.ui_doggo.dataSource.DoggoImagePagingSource
import org.koin.dsl.module

@ExperimentalPagingApi
val dataSourceModules = module {
    factory { UsersDataSourceFactory(get(), get()) }
    factory { DoggoImagePagingSource(get()) }
}