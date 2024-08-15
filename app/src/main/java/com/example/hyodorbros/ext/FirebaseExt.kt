package com.example.hyodorbros.ext

import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ui.community.board.BoardItem
import com.example.hyodorbros.ui.community.notification.NotificationItem
import com.example.hyodorbros.ui.community.question.QuestionItem
import com.example.hyodorbros.ui.community.report.ReportItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun FirebaseRepository.loginUser(
    email: String,
    password: String,
    callback: (isSuccess: Boolean) -> Unit
) {
    getFirebaseAuth().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            callback(it.isSuccessful)
        }
}

fun FirebaseRepository.getNotificationList(callback: (list: List<NotificationItem>?) -> Unit) {
    getFirebaseFireStore().collection("Notification").document("notification").get()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.exists()) {
                    val getResult: ArrayList<HashMap<String, String>>? =
                        it.result.get("list") as ArrayList<HashMap<String, String>>?
                    val toResultList = getResult?.map { it.toNotificationItem() }
                    if (!toResultList.isNullOrEmpty()) {
                        callback(toResultList)
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
}

fun HashMap<String, String>.toNotificationItem(): NotificationItem =
    NotificationItem(
        title = getValue("title"),
        content = getValue("content"),
        time = getValue("time")
    )


fun FirebaseRepository.getBoardList(type: String, callback: (list: List<BoardItem>?) -> Unit) {
    getFirebaseFireStore().collection("Board").document(type).get()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.exists()) {
                    val getResult: ArrayList<HashMap<String, String>>? =
                        it.result.get("list") as ArrayList<HashMap<String, String>>?
                    val toResultList = getResult?.map { it.toBoardItem() }
                    if (!toResultList.isNullOrEmpty()) {
                        callback(toResultList)
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
}

fun HashMap<String, String>.toBoardItem(): BoardItem =
    BoardItem(
        title = getValue("title"),
        content = getValue("content"),
        time = getValue("time"),
        image = getValue("image")
    )

fun FirebaseRepository.addReport(item: ReportItem, callback: (isSuccess: Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        addReport(item).addOnCompleteListener { dbResult ->
            callback(dbResult.isSuccessful)
        }
    }
}

fun FirebaseRepository.addQuestion(item: QuestionItem, callback: (isSuccess: Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        addQuestion(item).addOnCompleteListener { dbResult ->
            callback(dbResult.isSuccessful)
        }
    }
}

fun FirebaseRepository.addBoardItem(
    item: BoardItem,
    type: String,
    callback: (isSuccess: Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        addBoard(item, type).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }
}

fun FirebaseRepository.deleteBoardItem(
    item: BoardItem,
    type: String,
    callback: (isSuccess: Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        deleteBoard(item, type).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }
}
