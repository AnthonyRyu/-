package com.example.hyodorbros.ui.community.board

import com.example.hyodorbros.base.ViewState

sealed interface BoardViewState : ViewState {
    data class GetType(val type: String) : BoardViewState
    data class GetBoardList(val list: List<BoardItem>) : BoardViewState
    data class Error(val message: String) : BoardViewState
    data class RouteAdd(val type : String) : BoardViewState
}