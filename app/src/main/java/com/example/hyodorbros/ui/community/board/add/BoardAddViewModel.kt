package com.example.hyodorbros.ui.community.board.add

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.addBoardItem
import com.example.hyodorbros.ext.addReport
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.report.ReportItem
import com.example.hyodorbros.ui.community.report.ReportViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class BoardAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: FirebaseRepository
) :
    BaseViewModel() {

    private val getType = savedStateHandle.get<String>(KEY_TYPE).orEmpty()


    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")
    val inputImageLiveData = MutableLiveData("")

    fun save() {
        viewStateChanged(BoardAddViewState.ShowProgress)
        viewStateChanged(BoardAddViewState.EnableInput(false))
        ioScope {
            val checkTitle = async { checkTitle() }
            val checkContent = async { checkContent() }


            checkReport(checkTitle.await(), checkContent.await())?.let { board ->
                uploadPhoto(uri = Uri.parse(inputImageLiveData.value),
                    successHandler = {
                        firebaseRepository.addBoardItem(
                            board.copy(image = it),
                            getType
                        ) { isSuccess ->
                            if (isSuccess) {
                                viewStateChanged(BoardAddViewState.RouteBoard)
                                viewStateChanged(BoardAddViewState.HideProgress)
                            } else {
                                viewStateChanged(BoardAddViewState.Error("신고 접수를 실패하였습니다."))
                                viewStateChanged(BoardAddViewState.HideProgress)
                            }
                        }
                    }, errorHandler = {
                        firebaseRepository.addBoardItem(board, getType) { isSuccess ->
                            if (isSuccess) {
                                viewStateChanged(BoardAddViewState.RouteBoard)
                                viewStateChanged(BoardAddViewState.HideProgress)
                            } else {
                                viewStateChanged(BoardAddViewState.Error("신고 접수를 실패하였습니다."))
                                viewStateChanged(BoardAddViewState.HideProgress)
                            }
                        }
                    })
            } ?: viewStateChanged(BoardAddViewState.HideProgress)

            viewStateChanged(BoardAddViewState.EnableInput(true))
        }
    }

    private fun checkReport(
        checkTitle: Boolean,
        checkContent: Boolean,
    ): BoardItem? {
        return if (checkTitle && checkContent) {
            BoardItem(
                title = inputTitleLiveData.value!!,
                content = inputContentLiveData.value!!,
                image = inputImageLiveData.value!!
            )
        } else {
            null
        }
    }


    private fun checkTitle(): Boolean {
        return when {
            inputTitleLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(BoardAddViewState.Error("제목을 입력해 주세요."))
                false
            }
            else -> true
        }
    }


    private fun checkContent(): Boolean {
        return when {
            inputContentLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(BoardAddViewState.Error("내용를 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    errorHandler()
                }
            }
    }


    companion object {
        const val KEY_TYPE = "key_type"
    }
}