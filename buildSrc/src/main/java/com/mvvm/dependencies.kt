package com.mvvm

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.1"


    object Kotlin {
        private const val version = "1.4.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object AndroidX {
        const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val viewpager = "androidx.viewpager2:viewpager2:1.0.0"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-rc01"
        const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Coroutines {
            private const val version = "1.3.7"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }

        object Navigation {
            private const val version = "2.2.2"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Paging {
            private const val version = "2.1.2"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Paging3 {
            private const val version = "3.0.0-beta01"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Test {
            const val junit = "junit:junit:4.13.1"
            const val testUnit = "androidx.test.ext:junit:1.1.2"
            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0-alpha01"
    }

    object Koin {
        private const val version = "2.1.5"
        const val android = "org.koin:koin-android:$version"
        const val viewmodel = "org.koin:koin-androidx-viewmodel:$version"
        const val scope = "org.koin:koin-androidx-scope:$version"
        const val test = "org.koin:koin-test:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object OkHttp {
        private const val version = "4.7.2"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Rx {
        const val java = "io.reactivex.rxjava2:rxjava:2.2.16"
        const val android = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val kotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
        const val retrofit = "com.squareup.retrofit2:adapter-rxjava2:2.5.0"
    }

    object Moshi {
        private const val version = "1.9.2"
        const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object Coil {
        private const val version = "0.11.0"
        const val coil = "io.coil-kt:coil:$version"
    }

    object Glide {
        private const val version = "4.11.0"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
        const val glide = "com.github.bumptech.glide:glide:$version"
    }

    object Mockito {
        private const val version = "2.25.0"
        const val core = "org.mockito:mockito-core:$version"
    }

    object Arch {
        private const val version = "2.0.0"
        const val core = "android.arch.core:core-testing:$version"
    }

    object Insetter {
        private const val version = "0.3.0"
        const val core = "dev.chrisbanes:insetter:$version"
        const val dbx = "dev.chrisbanes:insetter-dbx:$version"
        const val ktx = "dev.chrisbanes:insetter-ktx:$version"
    }
}