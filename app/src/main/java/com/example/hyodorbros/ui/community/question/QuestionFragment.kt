package com.example.hyodorbros.ui.community.question

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentQuestionBinding
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import com.example.hyodorbros.ui.community.report.ReportViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionFragment :
    BaseFragment<FragmentQuestionBinding, QuestionViewModel, QuestionViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_question
    override val viewModel: QuestionViewModel by viewModels()


    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "문의 접수"
            }
        }
    }

    override fun onChangedViewState(viewState: QuestionViewState) {
        when (viewState) {
            is QuestionViewState.EnableInput -> {
                with(binding) {
                    reportEditText.isEnabled = viewState.isEnable
                    reportEditText2.isEnabled = viewState.isEnable
                    btnReportupload.isEnabled = viewState.isEnable
                }
            }
            is QuestionViewState.Error -> {
                showToast(message = viewState.message)
            }
            QuestionViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
            QuestionViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
            QuestionViewState.RouteMain -> {
                showToast(message = "문의 접수 되었습니다.")
                findNavController().popBackStack()
            }
            QuestionViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }
        }
    }
}