package com.andro.photoviewer.common.extensions

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(parameter = 0)
fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}