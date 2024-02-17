package com.andro.photoviewer.viewer.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.andro.photoviewer.R
import com.andro.photoviewer.common.extensions.attachSnapHelperWithListener
import com.andro.photoviewer.common.extensions.createMaterialAlertDialog
import com.andro.photoviewer.common.extensions.setImage
import com.andro.photoviewer.common.extensions.showToast
import com.andro.photoviewer.common.helper.OnSnapPositionChangeListener
import com.andro.photoviewer.common.helper.SnapOnScrollListener
import com.andro.photoviewer.databinding.ActivityViewerBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ViewerActivity : AppCompatActivity() {

    private val directorySelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val treeUri = result.data?.data
            if (result.resultCode == Activity.RESULT_OK && treeUri != null) {
                viewModel.handleDirectorySelection(this, treeUri)
            }
        }

    private val viewModel: ViewerViewModel by viewModels()
    private lateinit var binding: ActivityViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDialogToAskDirectory()

        setupUi()

        observeUi()
    }

    private fun showDialogToAskDirectory() {
        createMaterialAlertDialog(
            titleRes = R.string.select_directory_to_view,
            positiveActionMsg = R.string.okay,
            negativeActionMsg = R.string.cancel,
            positiveAction = {
                directorySelectionLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
            },
            negativeAction = {
                showToast("Closing app")
                finish()
            }
        ).show()
    }

    private fun observeUi() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                updateUi(it)
            }
        }
    }

    private fun setupUi() {
        binding.viewerRecyclerView.attachSnapHelperWithListener(
            LinearSnapHelper(),
            behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    val uri = viewModel.state.value.files.getOrNull(position)?.uri ?: return
                    binding.viewerImageView.setImage(uri)
                }
            }
        )

        binding.viewerRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildViewHolder(view).getAdapterPosition()
                if (position == 0 || position == state.itemCount - 1) {
                    val totalWidth = resources.displayMetrics.widthPixels / 2
                    val elementWidth = resources.getDimension(R.dimen.element_width).toInt() / 2
                    val elementMargin = resources.getDimension(R.dimen.element_margin).toInt() / 2
                    val padding = totalWidth - elementWidth - elementMargin
                    if (position == 0) {
                        outRect.left = padding
                    } else {
                        outRect.right = padding
                    }
                }
            }
        })
    }

    private fun updateUi(state: ViewerViewState) {
        binding.viewerProgressBar.isVisible = state.isLoading

        if (state.errorMessage != null) {
            showToast(state.errorMessage)
            showDialogToAskDirectory()
            return
        }

        val adapter = ViewerRecyclerAdapter(state.files)
        binding.viewerRecyclerView.adapter = adapter
    }
}