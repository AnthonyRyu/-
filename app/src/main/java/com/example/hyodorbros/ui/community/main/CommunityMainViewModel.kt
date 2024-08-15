package com.example.hyodorbros.ui.community.main

import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityMainViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    fun routeNotification() {
        viewStateChanged(CommunityMainViewState.RouteNotification)
    }

    fun routeQuestion() {
        viewStateChanged(CommunityMainViewState.RouteQuestion)
    }

    fun routeHomePage() {
        viewStateChanged(CommunityMainViewState.RouteHomePage)
    }

    fun routeReport() {
        viewStateChanged(CommunityMainViewState.RouteReport)
    }

    fun logout() {
        ioScope {
            if (firebaseRepository.logout()) {
                viewStateChanged(CommunityMainViewState.Logout)
            }
        }
    }

    fun routeFreeBoard() {
        viewStateChanged(CommunityMainViewState.RouteBoard("free"))
    }

    fun routeUsedBoard() {
        viewStateChanged(CommunityMainViewState.RouteBoard("used"))
    }

    fun routePresentBoard() {
        viewStateChanged(CommunityMainViewState.RouteBoard("present"))
    }

    fun withdraw() {
        ioScope {
            firebaseRepository.delete()?.addOnSuccessListener {
                viewStateChanged(CommunityMainViewState.WithDraw)
            }?.addOnCanceledListener {
                viewStateChanged(CommunityMainViewState.Error("회원탈퇴를 실패하였습니다."))
            }
        }
    }
}