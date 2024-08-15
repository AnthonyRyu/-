package com.example.hyodorbros.ui.community.board.update

import com.example.hyodorbros.base.ViewState

sealed interface BoardUpdateViewState : ViewState {
    object RouteBoard : BoardUpdateViewState
    object HideInputTextFiled : BoardUpdateViewState
    data class Error(val message: String) : BoardUpdateViewState
    data class EnableInput(val isEnable: Boolean) : BoardUpdateViewState
    object ShowProgress : BoardUpdateViewState
    object HideProgress : BoardUpdateViewState
    data class ShowImage(val url : String) : BoardUpdateViewState
}