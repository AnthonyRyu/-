package com.example.hyodorbros.ui.community.board.update

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.addBoardItem
import com.example.hyodorbros.ext.deleteBoardItem
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.board.add.BoardAddViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class BoardUpdateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: FirebaseRepository
) :
    BaseViewModel() {

    private val getType = savedStateHandle.get<String>(KEY_TYPE).orEmpty()
    private val getItem = savedStateHandle.get<BoardItem>(KEY_Item)

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    val inputTitleLiveData = MutableLiveData("")
    val inputContentLiveData = MutableLiveData("")
    val inputImageLiveData = MutableLiveData("")

    var isChangeImage = false

    init {
        getItem?.let {
            inputTitleLiveData.value = it.title
            inputContentLiveData.value = it.content
            inputImageLiveData.value = it.image

            if (it.image.isNotEmpty()) {
                viewStateChanged(BoardUpdateViewState.ShowImage(it.image))
            }
        }
    }

    fun update() {
        viewStateChanged(BoardUpdateViewState.ShowProgress)
        viewStateChanged(BoardUpdateViewState.EnableInput(false))
        ioScope {
            val checkTitle = async { checkTitle() }
            val checkContent = async { checkContent() }



            checkReport(checkTitle.await(), checkContent.await())?.let { board ->

                if (isChangeImage) {
                    uploadPhoto(uri = Uri.parse(inputImageLiveData.value),
                        successHandler = { image ->

                            firebaseRepository.addBoardItem(
                                board.copy(image = image),
                                getType
                            ) { isSuccess ->
                                if (isSuccess) {
                                    firebaseRepository.deleteBoardItem(getItem!!, getType) {
                                        if (it) {
                                            viewStateChanged(BoardUpdateViewState.RouteBoard)
                                            viewStateChanged(BoardUpdateViewState.HideProgress)
                                        } else {
                                            viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                                            viewStateChanged(BoardUpdateViewState.HideProgress)
                                        }
                                    }
                                } else {
                                    viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                                    viewStateChanged(BoardUpdateViewState.HideProgress)
                                }
                            }
                        }, errorHandler = {
                            firebaseRepository.addBoardItem(board, getType) { isSuccess ->
                                if (isSuccess) {
                                    firebaseRepository.deleteBoardItem(getItem!!, getType) {
                                        if (it) {
                                            viewStateChanged(BoardUpdateViewState.RouteBoard)
                                            viewStateChanged(BoardUpdateViewState.HideProgress)
                                        } else {
                                            viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                                            viewStateChanged(BoardUpdateViewState.HideProgress)
                                        }
                                    }
                                } else {
                                    viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                                    viewStateChanged(BoardUpdateViewState.HideProgress)
                                }
                            }
                        })
                } else {
                    firebaseRepository.addBoardItem(board, getType) { isSuccess ->
                        if (isSuccess) {
                            firebaseRepository.deleteBoardItem(getItem!!, getType) {
                                if (it) {
                                    viewStateChanged(BoardUpdateViewState.RouteBoard)
                                    viewStateChanged(BoardUpdateViewState.HideProgress)
                                } else {
                                    viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                                    viewStateChanged(BoardUpdateViewState.HideProgress)
                                }
                            }
                        } else {
                            viewStateChanged(BoardUpdateViewState.Error("수정을 실패하였습니다."))
                            viewStateChanged(BoardUpdateViewState.HideProgress)
                        }
                    }
                }


            } ?: viewStateChanged(BoardUpdateViewState.HideProgress)

            viewStateChanged(BoardUpdateViewState.EnableInput(true))
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
                viewStateChanged(BoardUpdateViewState.Error("제목을 입력해 주세요."))
                false
            }
            else -> true
        }
    }


    private fun checkContent(): Boolean {
        return when {
            inputContentLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(BoardUpdateViewState.Error("내용를 입력해 주세요."))
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
        const val KEY_Item = "key_item"
    }
}