package com.example.hyodorbros.ui.community.notification

import com.example.hyodorbros.base.ViewState

sealed interface NotificationViewState : ViewState {
    data class GetNotificationList(val list: List<NotificationItem>) : NotificationViewState
    object EmptyNotification : NotificationViewState
}