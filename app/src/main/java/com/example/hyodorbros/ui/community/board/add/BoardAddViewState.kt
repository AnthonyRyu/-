package com.example.hyodorbros.ui.community.board.add

import com.example.hyodorbros.base.ViewState

sealed interface BoardAddViewState : ViewState {
    object RouteBoard : BoardAddViewState
    object HideInputTextFiled : BoardAddViewState
    data class Error(val message: String) : BoardAddViewState
    data class EnableInput(val isEnable: Boolean) : BoardAddViewState
    object ShowProgress : BoardAddViewState
    object HideProgress : BoardAddViewState
}