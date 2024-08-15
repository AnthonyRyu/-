package com.example.hyodorbros.ui.community.board

import androidx.lifecycle.SavedStateHandle
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.deleteBoardItem
import com.example.hyodorbros.ext.getBoardList
import com.example.hyodorbros.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    val getType = savedStateHandle.get<String>(KEY_BOARD_TYPE).orEmpty()

    fun init() {
        viewStateChanged(BoardViewState.GetType(getType))
        ioScope {
            firebaseRepository.getBoardList(getType) { list ->
                if (!list.isNullOrEmpty()) {
                    val toSortList = list.sortedByDescending { it.time }
                    viewStateChanged(BoardViewState.GetBoardList(toSortList))
                } else{
                    viewStateChanged(BoardViewState.GetBoardList(emptyList()))
                }
            }
        }
    }

    fun routeAdd() {
        viewStateChanged(BoardViewState.RouteAdd(getType))
    }

    fun deleteBoard(item: BoardItem) {
        ioScope {
            firebaseRepository.deleteBoardItem(item, getType) { isSuccess ->
                if (isSuccess) {
                    init()
                } else {
                    viewStateChanged(BoardViewState.Error("삭제를 실패하였습니다."))
                }
            }
        }
    }

    companion object {
        const val KEY_BOARD_TYPE = "key_board_type"
    }
}