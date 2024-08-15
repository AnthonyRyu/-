package com.example.hyodorbros.ui.community.board.detail

import com.example.hyodorbros.base.ViewState
import com.example.hyodorbros.ui.community.board.BoardItem

sealed interface BoardDetailViewState : ViewState {
    data class GetItem(val item: BoardItem) : BoardDetailViewState
}