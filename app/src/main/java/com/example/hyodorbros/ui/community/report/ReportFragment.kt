package com.example.hyodorbros.ui.community.report

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentReportBinding
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding, ReportViewModel, ReportViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_report
    override val viewModel: ReportViewModel by viewModels()


    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "신고 접수"
            }
        }
    }

    override fun onChangedViewState(viewState: ReportViewState) {
        when (viewState) {
            is ReportViewState.EnableInput -> {
                with(binding) {
                    reportEditText.isEnabled = viewState.isEnable
                    reportEditText2.isEnabled = viewState.isEnable
                    btnReportupload.isEnabled = viewState.isEnable
                }
            }
            is ReportViewState.Error -> {
                showToast(message = viewState.message)
            }
            ReportViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            ReportViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
            ReportViewState.RouteMain -> {
                showToast(message = "신고 접수 되었습니다.")
                findNavController().popBackStack()
            }
            ReportViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }
        }
    }
}