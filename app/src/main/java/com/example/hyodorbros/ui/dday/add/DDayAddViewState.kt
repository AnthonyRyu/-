package com.example.hyodorbros.ui.dday.add

import com.example.hyodorbros.base.ViewState

sealed interface DDayAddViewState : ViewState {
    object ShowCalendar : DDayAddViewState
    object RouteMain : DDayAddViewState
    object HideInputTextFiled : DDayAddViewState
    data class Error(val message: String) : DDayAddViewState
}