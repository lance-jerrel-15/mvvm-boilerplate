package com.mvvm.boilerplate.di

import androidx.paging.ExperimentalPagingApi
import com.mvvm.boilerplate.ui.mainnav.MainViewModel
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.home.HomeViewModel
import com.mvvm.home.user.UserViewModel
import com.mvvm.login.LoginViewModel
import com.mvvm.ui_doggo.DoggoViewModel
import com.mvvm.ui_profile.ProfileViewModel
import com.mvvm.ui_profile.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalPagingApi
val viewModelModules = module {
    viewModel { SharedViewModel() }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { UserViewModel(get()) }
    viewModel { DoggoViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { RegisterViewModel() }
}