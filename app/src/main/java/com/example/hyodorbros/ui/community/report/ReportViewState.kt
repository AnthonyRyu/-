package com.example.hyodorbros.ui.community.report

import com.example.hyodorbros.base.ViewState

sealed interface ReportViewState : ViewState{
    object RouteMain : ReportViewState
    object HideInputTextFiled : ReportViewState
    data class Error(val message: String) : ReportViewState
    data class EnableInput(val isEnable: Boolean) : ReportViewState
    object ShowProgress : ReportViewState
    object HideProgress : ReportViewState
}