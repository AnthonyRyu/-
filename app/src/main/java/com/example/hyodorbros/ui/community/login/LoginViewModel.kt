package com.example.hyodorbros.ui.community.login

import androidx.lifecycle.MutableLiveData
import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.data.repo.FirebaseRepository
import com.example.hyodorbros.ext.ioScope
import com.example.hyodorbros.ext.loginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    BaseViewModel() {

    val inputEmailLiveData = MutableLiveData("")
    val inputPassLiveData = MutableLiveData("")

    fun login() {
        viewStateChanged(LoginViewState.ShowProgress)
        viewStateChanged(LoginViewState.EnableInput(false))
        ioScope {
            val checkEmail = async { checkEmail() }
            val checkPassword = async { checkPassword() }

            checkUser(checkEmail.await(), checkPassword.await())?.let { person ->

                /**
                 * 파이어베이스 로그인
                 */
                firebaseRepository.loginUser(
                    person.email,
                    person.password
                ) { isLogin ->
                    if (isLogin) {
                        /**
                         * 로그인 성공시, 홈화면 전환, 프로그래스바 숨기기
                         */
                        viewStateChanged(LoginViewState.RouteMain)
                        viewStateChanged(LoginViewState.HideProgress)
                    } else {
                        /**
                         * 로그인 실패시, 실패메시지 보여주기, 프로그래스바 숨기기
                         */
                        viewStateChanged(LoginViewState.Error("로그인을 실패하였습니다."))
                        viewStateChanged(LoginViewState.HideProgress)
                    }
                }
            } ?: viewStateChanged(LoginViewState.HideProgress)

            viewStateChanged(LoginViewState.EnableInput(true))
        }
    }

    fun routeFindPass() {
        viewStateChanged(LoginViewState.RouteFindPass)
    }

    fun routeRegister() {
        viewStateChanged(LoginViewState.RouteRegister)
    }

    /**
     * 입력한 id, password 체크 여부에 따른 결과 체크
     * 성공시, id, password 를 구성하는 Person 을 반환
     * 실패시, null 을 반환.
     */
    private fun checkUser(
        checkEmail: Boolean,
        checkPassword: Boolean,
    ): Person? {
        return if (checkEmail && checkPassword) {
            Person(inputEmailLiveData.value!!, inputPassLiveData.value!!)
        } else {
            null
        }
    }


    /**
     * 입력한 id 체크
     * 성공시 true, 실패시 false 반환.
     */
    private fun checkEmail(): Boolean {
        return when {
            inputEmailLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.Error("이메일을 입력해 주세요."))
                false
            }
            else -> true
        }
    }

    /**
     * 입력한 password 체크
     * 성공시 true, 실패시 false 반환.
     */
    private fun checkPassword(): Boolean {
        return when {
            inputPassLiveData.value.isNullOrEmpty() -> {
                viewStateChanged(LoginViewState.Error("비밀번호를 입력해 주세요."))
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