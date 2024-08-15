package com.example.hyodorbros.ui.dday.update

import com.example.hyodorbros.base.ViewState
import com.example.hyodorbros.room.entity.DDayEntity

sealed interface DDayUpdateViewState : ViewState {
    object ShowCalendar : DDayUpdateViewState
    object RouteMain : DDayUpdateViewState
    object HideInputTextFiled : DDayUpdateViewState
    data class ShowNotification(val item : DDayEntity) : DDayUpdateViewState
    data class HideNotification(val item : DDayEntity) : DDayUpdateViewState
    data class Error(val message: String) : DDayUpdateViewState
}