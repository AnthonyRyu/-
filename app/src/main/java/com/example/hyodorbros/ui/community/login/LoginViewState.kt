package com.example.hyodorbros.ui.community.login

import com.example.hyodorbros.base.ViewState

sealed interface LoginViewState : ViewState {
    object RouteMain : LoginViewState
    object RouteRegister : LoginViewState
    object RouteFindPass : LoginViewState
    object HideInputTextFiled : LoginViewState
    data class Error(val message: String) : LoginViewState
    data class EnableInput(val isEnable: Boolean) : LoginViewState
    object ShowProgress : LoginViewState
    object HideProgress : LoginViewState
}