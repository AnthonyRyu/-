package com.example.hyodorbros.ui.community.findpass

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentFindPassBinding
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FindPassFragment :
    BaseFragment<FragmentFindPassBinding, FindPassViewModel, FindPassViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_find_pass
    override val viewModel: FindPassViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "비밀번호 초기화"
            }
        }
    }

    override fun onChangedViewState(viewState: FindPassViewState) {
        when (viewState) {
            is FindPassViewState.EnableInput -> {
                with(binding) {
                    sendEmail.isEnabled = viewState.isEnable
                    btnReset.isEnabled = viewState.isEnable
                }
            }
            is FindPassViewState.Error -> {
                showToast(message = viewState.message)
            }

            FindPassViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }

            FindPassViewState.RouteLogin -> {
                showToast(message = "가입한 메일로 초기화 가능한 메일이 전송되었습니다.")
                findNavController().popBackStack()
            }
        }
    }
}