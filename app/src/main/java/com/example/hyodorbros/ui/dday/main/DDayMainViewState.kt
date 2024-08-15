package com.example.hyodorbros.ui.dday.main

import com.example.hyodorbros.base.ViewState
import com.example.hyodorbros.room.entity.DDayEntity
import com.example.hyodorbros.ui.home.HomeViewState

sealed interface DDayMainViewState : ViewState{
    object RouteAdd : DDayMainViewState
    data class RouteUpdate(val item : DDayEntity) : DDayMainViewState
    data class ShowNotification(val item : DDayEntity) : DDayMainViewState
    data class HideNotification(val item : DDayEntity) : DDayMainViewState
}