package com.example.hyodorbros.ui.community.notification

import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.getNotificationList
import com.example.hyodorbros.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    init {
        getNotificationList()
    }

    private fun getNotificationList() {
        ioScope {
            firebaseRepository.getNotificationList { list ->
                if (!list.isNullOrEmpty()) {
                    val toSort = list.sortedByDescending { it.time }
                    viewStateChanged(NotificationViewState.GetNotificationList(toSort))
                } else {
                    viewStateChanged(NotificationViewState.EmptyNotification)
                }
            }
        }
    }
}