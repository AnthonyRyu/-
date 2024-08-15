package com.example.hyodorbros.ui.community.main

import com.example.hyodorbros.base.ViewState

sealed interface CommunityMainViewState : ViewState {
    data class Error(val message: String) : CommunityMainViewState
    object RouteNotification :CommunityMainViewState
    object RouteQuestion :CommunityMainViewState
    object RouteHomePage :CommunityMainViewState
    object RouteReport :CommunityMainViewState
    object Logout : CommunityMainViewState
    object WithDraw : CommunityMainViewState
    data class RouteBoard(val type : String) : CommunityMainViewState
}