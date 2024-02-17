package com.andro.photoviewer.viewer.ui

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro.photoviewer.viewer.domain.FilesFromPathUseCase
import com.andro.photoviewer.viewer.model.FileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewerViewModel @JvmOverloads constructor(
    private val filesFromPathUseCase: FilesFromPathUseCase = FilesFromPathUseCase()
) : ViewModel() {

    private val filesData = MutableStateFlow(listOf<FileData>())
    private val errorMessage = MutableStateFlow<String?>(null)

    val state: StateFlow<ViewerViewState> = combine(
        filesData,
        errorMessage,
        ::ViewerViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ViewerViewState.EMPTY,
    )

    fun handleDirectorySelection(context: Context, treeUri: Uri) {
        val pickedDirectory = DocumentFile.fromTreeUri(context, treeUri) ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val files = filesFromPathUseCase.trySync(
                FilesFromPathUseCase.Params(pickedDirectory)
            ) ?: run {
                errorMessage.value = "Something went wrong, try selecting the directory again!"
                return@launch
            }

            if (files.isEmpty()) {
                errorMessage.value =
                    "No supported files present in this directory, choose another directory"
                return@launch
            }

            filesData.value = files
        }
    }
}