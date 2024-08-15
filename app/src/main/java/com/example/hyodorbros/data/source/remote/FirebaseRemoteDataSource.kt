package com.example.hyodorbros.data.source.remote

import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.question.QuestionItem
import com.example.hyodorbros.ui.community.report.ReportItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


interface FirebaseRemoteDataSource {

    suspend fun login(
        id: String,
        password: String
    ): Task<AuthResult>

    suspend fun logout(): Boolean

    suspend fun register(
        id: String,
        password: String
    ): Task<AuthResult>

    suspend fun delete(): Task<Void>?

    suspend fun resetPass(
        resetPassToId: String
    ): Task<Void>

    suspend fun createWordDB(
        id: String
    ): Task<Void>


    suspend fun addReport(
        item: ReportItem
    ): Task<Void>


    suspend fun addQuestion(
        item: QuestionItem
    ): Task<Void>

    suspend fun addBoard(
        item : BoardItem,
        type: String
    ): Task<Void>

    suspend fun deleteBoard(
        item : BoardItem,
        type: String
    ): Task<Void>

    fun getFirebaseAuth(): FirebaseAuth

    fun getFirebaseFireStore(): FirebaseFirestore

}