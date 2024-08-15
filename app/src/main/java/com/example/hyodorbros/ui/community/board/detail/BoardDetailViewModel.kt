package com.example.hyodorbros.ui.community.board.detail

import androidx.lifecycle.SavedStateHandle
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.ui.community.board.BoardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

    private val getItem = savedStateHandle.get<BoardItem>(KEY_BOARD)


    init {
        getItem?.let {
            viewStateChanged(BoardDetailViewState.GetItem(it))
        }
    }

    companion object {
        const val KEY_BOARD = "key_board"
    }
}