package com.mvvm.boilerplate.di

import android.app.Application
import com.mvvm.boilerplate.util.API_ENDPOINT
import com.mvvm.boilerplate.util.API_KEY
import com.mvvm.boilerplate.util.BASE_URL
import com.mvvm.boilerplate.util.CACHE_SIZE
import com.mvvm.boilerplate.util.CODE_UNAUTHORIZED
import com.mvvm.boilerplate.util.HEADER_API_KEY
import com.mvvm.boilerplate.util.TIMEOUT_SECONDS
import com.mvvm.commons.livedatabus.Event
import com.mvvm.commons.livedatabus.LiveDataBus
import com.mvvm.data.services.DoggoService
import com.mvvm.data.services.ReqresService
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModules = module {
    single { provideCache(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }

    single { provideLoginService(get()) }
    single { provideDoggoApi(get()) }
}


private fun provideCache(application: Application) =
    Cache(File(application.cacheDir, "http-cache"), CACHE_SIZE)

private fun provideOkHttpClient(cache: Cache, okHttpNetworkInterceptor: Interceptor = getOkHttpNetworkInterceptor()) =
    OkHttpClient.Builder().apply {
        cache(cache)
        //        if (BuildConfig.DEBUG) {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        //        }

        addInterceptor(okHttpNetworkInterceptor)
        addNetworkInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == CODE_UNAUTHORIZED) {
                LiveDataBus.publish(Event.UNAUTHORIZED)
            }
            response
        }
        connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }.build()

private fun getOkHttpNetworkInterceptor(): Interceptor {
    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest =
                chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
            return chain.proceed(newRequest)
        }
    }
}

private fun provideDoggoApi(okHttpClient: OkHttpClient) =
    Retrofit.Builder()
        .baseUrl(API_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(DoggoService::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

private fun provideLoginService(retrofit: Retrofit) =
    retrofit.create(ReqresService::class.java)