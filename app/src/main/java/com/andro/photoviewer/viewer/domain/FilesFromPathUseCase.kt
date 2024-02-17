package com.andro.photoviewer.viewer.domain

import androidx.documentfile.provider.DocumentFile
import com.andro.photoviewer.common.domain.ResultUseCase
import com.andro.photoviewer.viewer.model.FileData

class FilesFromPathUseCase : ResultUseCase<FilesFromPathUseCase.Params, List<FileData>>() {

    override suspend fun doWork(params: Params): List<FileData> {
        try {
            val pickedDirectory = params.pickedDirectory
            if (pickedDirectory.isDirectory.not()) {
                throw Error("Directory doesn't exist or not a valid directory")
            }

            return pickedDirectory.listFiles().mapNotNull { file ->
                file ?: return@mapNotNull null
                FileData(
                    uri = file.uri,
                    name = file.name ?: ""
                )
            }.filter { fileData ->
                val format = fileData.name.substring(
                    fileData.name.lastIndexOf(".") + 1
                )
                supportedFormats.contains(format.lowercase())
            }
        } catch (e: Exception) {
            throw Error("Error while reading directory: ${e.message}")
        }
    }

    data class Params(
        val pickedDirectory: DocumentFile
    )

    companion object {
        val supportedFormats = listOf("jpeg", "png", "jpg")
    }
}