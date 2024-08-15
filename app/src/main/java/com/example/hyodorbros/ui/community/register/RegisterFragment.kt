package com.example.hyodorbros.ui.community.register

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentRegisterBinding
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, RegisterViewModel, RegisterViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_register
    override val viewModel: RegisterViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel
    }

    override fun onChangedViewState(viewState: RegisterViewState) {
        when (viewState) {
            is RegisterViewState.EnableInput -> {
                with(binding) {
                    etEmail.isEnabled = viewState.isEnable
                    etName.isEnabled = viewState.isEnable
                    etNumber.isEnabled = viewState.isEnable
                    etPwd.isEnabled = viewState.isEnable
                    etUni.isEnabled = viewState.isEnable
                    btnRegister.isEnabled = viewState.isEnable
                }
            }
            is RegisterViewState.Error -> {
                showToast(message = viewState.message)
            }
            RegisterViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            RegisterViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
            RegisterViewState.RouteLogin -> {
                showToast(message = "회원가입을 성공하였습니다.")
                findNavController().popBackStack()
            }
            RegisterViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }
        }
    }
}