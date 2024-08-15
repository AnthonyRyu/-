package com.example.hyodorbros.ui.community.register

import com.example.hyodorbros.base.ViewState

sealed interface RegisterViewState : ViewState {
    object RouteLogin : RegisterViewState
    object HideInputTextFiled : RegisterViewState
    data class Error(val message: String) : RegisterViewState
    data class EnableInput(val isEnable: Boolean) : RegisterViewState
    object ShowProgress : RegisterViewState
    object HideProgress : RegisterViewState
}