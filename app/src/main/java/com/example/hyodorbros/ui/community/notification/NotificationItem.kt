package com.example.hyodorbros.ui.community.notification

data class NotificationItem(
    val title: String,
    val content: String,
    val time: String = System.currentTimeMillis().toString()
)
