package com.example.hyodorbros.data.source.remote

import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.notification.NotificationItem
import com.example.hyodorbros.ui.community.question.QuestionItem
import com.example.hyodorbros.ui.community.report.ReportItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) :
    FirebaseRemoteDataSource {


    override suspend fun login(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.signInWithEmailAndPassword(id, password)
        }

    override suspend fun logout(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext try {
                firebaseAuth.signOut()
                firebaseAuth.currentUser == null
            } catch (e: Exception) {
                false
            }
        }

    override suspend fun register(id: String, password: String): Task<AuthResult> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.createUserWithEmailAndPassword(
                id,
                password
            )
        }


    override suspend fun delete(): Task<Void>? = withContext(Dispatchers.IO) {
        return@withContext firebaseAuth.currentUser?.delete()
    }

    override suspend fun resetPass(resetPassToId: String): Task<Void> =
        withContext(Dispatchers.IO) {
            return@withContext firebaseAuth.sendPasswordResetEmail(resetPassToId)
        }

    override suspend fun createWordDB(id: String): Task<Void> {
        return fireStore.collection("Board").document("present")
            .set(emptyMap<String, BoardItem>())
    }

    override suspend fun addReport(item: ReportItem): Task<Void> {
        return fireStore.collection("Report").document("report")
            .update("list", FieldValue.arrayUnion(item))
    }

    override suspend fun addQuestion(item: QuestionItem): Task<Void> {
        return fireStore.collection("Question").document("question")
            .update("list", FieldValue.arrayUnion(item))
    }

    override suspend fun addBoard(item: BoardItem, type: String): Task<Void> {
        return fireStore.collection("Board").document(type)
            .update("list", FieldValue.arrayUnion(item))
    }

    override suspend fun deleteBoard(item: BoardItem, type: String): Task<Void> {
        return fireStore.collection("Board").document(type)
            .update("list", FieldValue.arrayRemove(item))
    }

    override fun getFirebaseAuth(): FirebaseAuth =
        firebaseAuth

    override fun getFirebaseFireStore(): FirebaseFirestore =
        fireStore

}