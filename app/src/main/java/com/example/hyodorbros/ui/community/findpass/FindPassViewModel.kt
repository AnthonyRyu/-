package com.example.hyodorbros.ui.community.findpass

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class FindPassViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    val inputEmailLiveData = MutableLiveData("")

    fun resetPassword() {
        viewStateChanged(FindPassViewState.ShowProgress)
        viewStateChanged(FindPassViewState.EnableInput(false))
        ioScope {
            val checkEmail = async { checkEmail() }.await()

            if (checkEmail) {
                firebaseRepository.resetPass(inputEmailLiveData.value!!)
                    .addOnCompleteListener {
                        viewStateChanged(FindPassViewState.HideProgress)
                        viewStateChanged(FindPassViewState.EnableInput(true))
                    }
                    .addOnSuccessListener {
                        viewStateChanged(FindPassViewState.RouteLogin)
                    }.addOnCanceledListener {
                        viewStateChanged(FindPassViewState.Error("올바른 이메일을 입력하세요."))
                    }
            }
        }
    }


    private fun checkEmail(): Boolean {
        return when {
            inputEmailLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(FindPassViewState.Error("이메일을 입력해 주세요."))
                false
            }
            else -> true
        }
    }
}