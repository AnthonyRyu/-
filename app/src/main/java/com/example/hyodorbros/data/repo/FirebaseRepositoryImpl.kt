package com.example.hyodorbros.data.repo


import com.example.hyodorbros.data.source.remote.FirebaseRemoteDataSource
import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.notification.NotificationItem
import com.example.hyodorbros.ui.community.question.QuestionItem
import com.example.hyodorbros.ui.community.report.ReportItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class FirebaseRepositoryImpl @Inject constructor(private val firebaseRemoteDataSource: FirebaseRemoteDataSource) :
    FirebaseRepository {

    override suspend fun login(id: String, password: String): Task<AuthResult> =
        firebaseRemoteDataSource.login(id, password)

    override suspend fun logout(): Boolean =
        firebaseRemoteDataSource.logout()

    override suspend fun register(id: String, password: String): Task<AuthResult> =
        firebaseRemoteDataSource.register(id, password)

    override suspend fun delete(): Task<Void>? =
        firebaseRemoteDataSource.delete()

    override suspend fun resetPass(resetPassToId: String): Task<Void> =
        firebaseRemoteDataSource.resetPass(resetPassToId)

    override suspend fun createWordDB(id: String): Task<Void> =
        firebaseRemoteDataSource.createWordDB(id)

    //    override suspend fun createWordDB(id: String): Task<Void> =
//        firebaseRemoteDataSource.createWordDB(id)
//

    override suspend fun addReport(item: ReportItem): Task<Void> =
        firebaseRemoteDataSource.addReport(item)

    override suspend fun addQuestion(item: QuestionItem): Task<Void> =
        firebaseRemoteDataSource.addQuestion(item)

    //
//    override suspend fun deleteWordItem(id: String, wordItem: BookmarkWord): Task<Void> =
//        firebaseRemoteDataSource.deleteWordItem(id, wordItem)

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseRemoteDataSource.getFirebaseAuth()

    override fun getFirebaseFireStore(): FirebaseFirestore =
        firebaseRemoteDataSource.getFirebaseFireStore()

    override suspend fun addBoard(item: BoardItem, type: String): Task<Void> =
        firebaseRemoteDataSource.addBoard(item, type)

    override suspend fun deleteBoard(item: BoardItem, type: String): Task<Void> =
        firebaseRemoteDataSource.deleteBoard(item, type)
}