package com.example.hyodorbros.ui.community.question

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.addQuestion
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ui.community.report.ReportViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")

    fun question() {
        viewStateChanged(QuestionViewState.ShowProgress)
        viewStateChanged(QuestionViewState.EnableInput(false))
        ioScope {
            val checkTitle = async { checkTitle() }
            val checkContent = async { checkContent() }

            checkQuestionItem(checkTitle.await(), checkContent.await())?.let { question ->
                firebaseRepository.addQuestion(question) { isSuccess ->
                    if (isSuccess) {
                        viewStateChanged(QuestionViewState.RouteMain)
                        viewStateChanged(QuestionViewState.HideProgress)
                    } else {
                        viewStateChanged(QuestionViewState.Error("신고 접수를 실패하였습니다."))
                        viewStateChanged(QuestionViewState.HideProgress)
                    }
                }
            } ?: viewStateChanged(QuestionViewState.HideProgress)

            viewStateChanged(QuestionViewState.EnableInput(true))
        }
    }

    private fun checkQuestionItem(
        checkTitle: Boolean,
        checkContent: Boolean,
    ): QuestionItem? {
        return if (checkTitle && checkContent) {
            QuestionItem(inputTitleLiveData.value!!, inputContentLiveData.value!!)
        } else {
            null
        }
    }


    private fun checkTitle(): Boolean {
        return when {
            inputTitleLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(ReportViewState.Error("제목을 입력해 주세요."))
                false
            }
            else -> true
        }
    }


    private fun checkContent(): Boolean {
        return when {
            inputContentLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(ReportViewState.Error("내용를 입력해 주세요."))
                false
            }
            else -> true
        }
    }

}