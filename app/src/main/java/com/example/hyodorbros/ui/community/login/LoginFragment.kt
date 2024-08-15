package com.example.hyodorbros.ui.community.login

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentLoginBinding
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel, LoginViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel
    }

    override fun onChangedViewState(viewState: LoginViewState) {
        when (viewState) {
            is LoginViewState.Error -> {
                showToast(message = viewState.message)
            }
            LoginViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            LoginViewState.RouteFindPass -> {
                findNavController().navigate(R.id.action_frg_login_to_frg_find_pass)
            }
            LoginViewState.RouteMain -> {
                with(binding) {
                    etEmail.text.clear()
                    etPwd.text.clear()
                }
                findNavController().navigate(R.id.action_frg_login_to_frg_main)
            }
            LoginViewState.RouteRegister -> {
                findNavController().navigate(R.id.action_frg_login_to_frg_register)
            }
            is LoginViewState.EnableInput -> {
                with(binding) {
                    etEmail.isEnabled = viewState.isEnable
                    etPwd.isEnabled = viewState.isEnable
                    btnOk.isEnabled = viewState.isEnable
                }
            }
            /**
             * Progress Show
             */
            is LoginViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            /**
             * Progress Hide
             */
            is LoginViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }
}