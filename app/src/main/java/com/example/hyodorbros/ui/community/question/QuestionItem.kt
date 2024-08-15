package com.example.hyodorbros.ui.community.question

data class QuestionItem(
    val title: String,
    val content: String,
    val time: Long = System.currentTimeMillis()
)