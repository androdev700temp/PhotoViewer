package com.andro.photoviewer.common.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.andro.photoviewer.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.createMaterialAlertDialog(
    @StyleRes themeId: Int? = null,
    @StringRes titleRes: Int = R.string.empty_string,
    @StringRes messageRes: Int = R.string.empty_string,
    @StringRes negativeActionMsg: Int = R.string.empty_string,
    @StringRes positiveActionMsg: Int = R.string.empty_string,
    positiveAction: () -> Unit = {},
    negativeAction: () -> Unit = {},
    cancelable: Boolean = true
): MaterialAlertDialogBuilder {
    val dialogBuilder = if (themeId == null) {
        MaterialAlertDialogBuilder(this).setCancelable(cancelable)
    } else {
        MaterialAlertDialogBuilder(this, themeId).setCancelable(cancelable)
    }

    if (titleRes != R.string.empty_string) {
        dialogBuilder.setTitle(titleRes)
    }

    if (messageRes != R.string.empty_string) {
        dialogBuilder.setMessage(messageRes)
    }

    if (negativeActionMsg != R.string.empty_string) {
        dialogBuilder.setNegativeButton(negativeActionMsg) { dialog, _ ->
            negativeAction()
            dialog.dismiss()
        }
    }

    if (positiveActionMsg != R.string.empty_string) {
        dialogBuilder.setPositiveButton(positiveActionMsg) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
    }

    return dialogBuilder
}

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()