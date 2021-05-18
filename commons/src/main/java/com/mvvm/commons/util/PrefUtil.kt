package com.mvvm.commons.util

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

const val PREF_MAIN = "mvvm"
const val PREF_BEARER_TOKEN = "com.mvvm.BEARER_TOKEN"
const val PREF_NAME = "com.mvvm.NAME"
const val PREF_EMAIL = "com.mvvm.EMAIL"

fun Fragment.getPref(): SharedPreferences? =
    context?.getSharedPreferences(PREF_MAIN, Context.MODE_PRIVATE)

fun Context.getPref(): SharedPreferences =
    getSharedPreferences(PREF_MAIN, Context.MODE_PRIVATE)

fun SharedPreferences.updateToken(token: String) =
    edit().putString(PREF_BEARER_TOKEN, token).apply()

fun SharedPreferences.isLoggedIn() =
    !(getString(PREF_BEARER_TOKEN, null).isNullOrBlank())

fun SharedPreferences.updateName(name: String) =
    edit().putString(PREF_NAME, name).apply()

fun SharedPreferences.updateEmail(name: String) =
    edit().putString(PREF_EMAIL, name).apply()

fun SharedPreferences.clear() =
    edit().clear().commit()
