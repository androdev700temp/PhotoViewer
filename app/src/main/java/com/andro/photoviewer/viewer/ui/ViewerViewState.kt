package com.andro.photoviewer.viewer.ui

import com.andro.photoviewer.viewer.model.FileData

data class ViewerViewState(
    val files: List<FileData>,
    val errorMessage: String?,
) {
    companion object {
        val EMPTY = ViewerViewState(
            files = listOf(),
            errorMessage = null,
        )
    }
}