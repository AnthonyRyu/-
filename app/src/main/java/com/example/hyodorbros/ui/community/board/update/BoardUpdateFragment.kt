package com.example.hyodorbros.ui.community.board.update

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentBoardUpdateBinding
import com.example.hyodorbros.ext.checkPermission
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardUpdateFragment :
    BaseFragment<FragmentBoardUpdateBinding, BoardUpdateViewModel, BoardUpdateViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_board_update
    override val viewModel: BoardUpdateViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel

        binding.btnImage3.setOnClickListener {

            requireContext().checkPermission { isGrant ->
                if (isGrant) {
                    choosePhoto.launch("image/Pictures/*")
                }
            }
        }
    }

    private val choosePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.inputImageLiveData.value = it.toString()
        viewModel.isChangeImage = true
        Glide.with(this).load(it).into(binding.image)
    }


    override fun onChangedViewState(viewState: BoardUpdateViewState) {
        when (viewState) {
            is BoardUpdateViewState.EnableInput -> {
                with(binding) {
                    titleEditText3.isEnabled = viewState.isEnable
                    titleEditText4.isEnabled = viewState.isEnable
                    btnUpload3.isEnabled = viewState.isEnable
                    btnImage3.isEnabled = viewState.isEnable
                }
            }
            is BoardUpdateViewState.Error -> {
                showToast(message = viewState.message)
            }

            BoardUpdateViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            BoardUpdateViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
            BoardUpdateViewState.RouteBoard -> {
                showToast(message = "수정되었습니다.")
                findNavController().popBackStack()
            }
            BoardUpdateViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }
            is BoardUpdateViewState.ShowImage -> {
                Glide.with(this).load(viewState.url).into(binding.image)
            }
        }
    }
}
