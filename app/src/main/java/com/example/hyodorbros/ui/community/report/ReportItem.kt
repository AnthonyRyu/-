package com.example.hyodorbros.ui.community.report

data class ReportItem(
    val title: String,
    val content: String,
    val time: Long = System.currentTimeMillis()
)
