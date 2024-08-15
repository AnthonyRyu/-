package com.example.hyodorbros.ui.community.question

import com.example.hyodorbros.base.ViewState

sealed interface QuestionViewState : ViewState{
    object RouteMain : QuestionViewState
    object HideInputTextFiled : QuestionViewState
    data class Error(val message: String) : QuestionViewState
    data class EnableInput(val isEnable: Boolean) : QuestionViewState
    object ShowProgress : QuestionViewState
    object HideProgress : QuestionViewState
}