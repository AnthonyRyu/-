package com.example.hyodorbros.ui.community.board.add

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentBoardAddBinding
import com.example.hyodorbros.ext.checkPermission
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardAddFragment :
    BaseFragment<FragmentBoardAddBinding, BoardAddViewModel, BoardAddViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_board_add
    override val viewModel: BoardAddViewModel by viewModels()


    private val choosePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.inputImageLiveData.value = it.toString()
        Glide.with(this).load(it).into(binding.image)
    }


    override fun initUi() {
        binding.viewModel = viewModel

        with(binding.containerToolbar) {
            toolbar.setBackgroundColor(R.color.toolbar)
            title.text = "게시판 글쓰기"
        }

        binding.btnImage3.setOnClickListener {

            requireContext().checkPermission { isGrant ->
                if (isGrant) {
                    choosePhoto.launch("image/Pictures/*")
                }
            }
        }

    }

    override fun onChangedViewState(viewState: BoardAddViewState) {
        when (viewState) {
            is BoardAddViewState.EnableInput -> {
                with(binding) {
                    titleEditText3.isEnabled = viewState.isEnable
                    titleEditText4.isEnabled = viewState.isEnable
                    btnUpload3.isEnabled = viewState.isEnable
                    btnImage3.isEnabled = viewState.isEnable
                }

            }
            is BoardAddViewState.Error -> {
                showToast(message = viewState.message)
            }
            BoardAddViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            BoardAddViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
            BoardAddViewState.RouteBoard -> {
                findNavController().popBackStack()
            }
            BoardAddViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }
        }
    }
}