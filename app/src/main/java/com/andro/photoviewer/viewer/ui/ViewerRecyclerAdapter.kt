package com.andro.photoviewer.viewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andro.photoviewer.common.extensions.setImage
import com.andro.photoviewer.databinding.ItemViewerRecyclerItemBinding
import com.andro.photoviewer.viewer.model.FileData

class ViewerRecyclerAdapter(
    private val data: List<FileData>
) : RecyclerView.Adapter<ViewerRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewerRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemViewerRecyclerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: FileData) {
            binding.pagerRecyclerImageView.setImage(data.uri)
        }
    }
}