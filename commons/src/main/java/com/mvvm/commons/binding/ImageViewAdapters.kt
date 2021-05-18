package com.mvvm.commons.binding

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@SuppressLint("CheckResult")
@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) {
        Glide.with(view).load(url)
            .thumbnail(0.1f).into(view)
    } else {
        Glide.with(view).clear(view)
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("loadImageCircleCrop")
fun loadImageCircleCrop(view: ImageView, url: String?) {
    if (url != null) {
        Glide.with(view).load(url)
            .thumbnail(0.1f)
            .centerCrop().circleCrop().into(view)
    } else {
        Glide.with(view).clear(view)
    }
}

@SuppressLint("CheckResult")
@BindingAdapter("loadImageCenterCrop")
fun loadImageCenterCrop(view: ImageView, url: String?) {
    if (url != null) {
        Glide.with(view)
            .load(url)
            .thumbnail(0.1f)
            .centerCrop()
            .into(view)
    } else {
        Glide.with(view).clear(view)
    }
}
