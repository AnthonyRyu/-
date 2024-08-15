package com.example.hyodorbros.ui.community.findpass

import com.example.hyodorbros.base.ViewState

sealed interface FindPassViewState : ViewState{
    object RouteLogin : FindPassViewState
    object HideInputTextFiled : FindPassViewState
    data class Error(val message: String) : FindPassViewState
    data class EnableInput(val isEnable: Boolean) : FindPassViewState
    object ShowProgress : FindPassViewState
    object HideProgress : FindPassViewState
}