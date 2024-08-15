package com.example.hyodorbros.ui.community.board

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardItem(
    val title: String,
    val content: String,
    val image: String,
    val time: String = System.currentTimeMillis().toString()
) : Parcelable