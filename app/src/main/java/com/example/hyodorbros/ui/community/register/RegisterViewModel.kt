package com.example.hyodorbros.ui.community.register

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    /**
     * id, password, passwordOk 입력하는 값을 가져오는 변수들
     */
    val inputEmailLiveData = MutableLiveData<String>()
    val inputPasswordLiveData = MutableLiveData<String>()
    val inputNickNameLiveData = MutableLiveData<String>()
    val inputSchoolLiveData = MutableLiveData<String>()
    val inputSchoolNumLiveData = MutableLiveData<String>()


    /**
     * 회원가입 로직.
     * 로그인 방식을 참조하여 확인할 것.
     */
    fun register() {
        ioScope {
            viewStateChanged(RegisterViewState.ShowProgress)
            viewStateChanged(RegisterViewState.EnableInput(false))
            val checkEmail = async { checkEmail() }
            val checkPassword = async { checkPassword() }
            val checkNickName = async { checkNickName() }
            val checkSchool = async { checkSchool() }
            val checkSchoolNum = async { checkSchoolNum() }

            checkUser(
                checkEmail.await(),
                checkPassword.await(),
                checkNickName.await(),
                checkSchool.await(),
                checkSchoolNum.await()
            )?.let { person ->
                firebaseRepository.register(
                    person.email,
                    person.password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        viewStateChanged(RegisterViewState.RouteLogin)
                        viewStateChanged(RegisterViewState.HideProgress)
                    } else {
                        viewStateChanged(RegisterViewState.Error("회원가입을 실패하였습니다."))
                        viewStateChanged(RegisterViewState.HideProgress)
                    }
                }
            } ?: viewStateChanged(RegisterViewState.HideProgress)

            viewStateChanged(RegisterViewState.EnableInput(true))
        }
    }


    private fun checkUser(
        checkEmail: Boolean,
        checkPassword: Boolean,
        checkNickName: Boolean,
        checkSchool: Boolean,
        checkSchoolNum: Boolean,

        ): Person? {
        return if (checkEmail && checkPassword && checkNickName && checkSchool && checkSchoolNum) {
            Person(inputEmailLiveData.value!!, inputPasswordLiveData.value!!)
        } else {
            null
        }
    }

    private fun checkEmail(): Boolean {
        return when {
            inputEmailLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.Error("이메일을 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    private fun checkPassword(): Boolean {
        return when {
            inputPasswordLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.Error("비밀번호를 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    private fun checkNickName(): Boolean {
        return when {
            inputNickNameLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.Error("닉네임을 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    private fun checkSchool(): Boolean {
        return when {
            inputSchoolLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.Error("학교를 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    private fun checkSchoolNum(): Boolean {
        return when {
            inputSchoolNumLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(RegisterViewState.Error("학번를 입력해 주세요."))
                false
            }
            else -> true
        }
    }


    data class Person(
        val email: String,
        val password: String
    )

}