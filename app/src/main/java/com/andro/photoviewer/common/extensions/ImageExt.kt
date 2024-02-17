package com.andro.photoviewer.common.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImage(uri: Uri, cornerRadius: Int = 0) {
    var requestOptions = RequestOptions()
    if (cornerRadius != 0) {
        requestOptions = requestOptions.transform(RoundedCorners(cornerRadius))
    }
    Glide.with(this.context)
        .load(uri)
        .apply(requestOptions)
        .into(this)
}