package com.example.hyodorbros.ui.community.report

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.addReport
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ext.loginUser
import com.example.hyodorbros.ui.community.login.LoginViewModel
import com.example.hyodorbros.ui.community.login.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")


    fun report() {
        viewStateChanged(ReportViewState.ShowProgress)
        viewStateChanged(ReportViewState.EnableInput(false))
        ioScope {
            val checkTitle = async { checkTitle() }
            val checkContent = async { checkContent() }

            checkReport(checkTitle.await(), checkContent.await())?.let { report ->

                firebaseRepository.addReport(report) { isSuccess ->
                    if (isSuccess) {
                        viewStateChanged(ReportViewState.RouteMain)
                        viewStateChanged(ReportViewState.HideProgress)
                    } else {
                        viewStateChanged(ReportViewState.Error("신고 접수를 실패하였습니다."))
                        viewStateChanged(ReportViewState.HideProgress)
                    }
                }
            } ?: viewStateChanged(ReportViewState.HideProgress)

            viewStateChanged(ReportViewState.EnableInput(true))
        }
    }

    private fun checkReport(
        checkTitle: Boolean,
        checkContent: Boolean,
    ): ReportItem? {
        return if (checkTitle && checkContent) {
            ReportItem(inputTitleLiveData.value!!, inputContentLiveData.value!!)
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