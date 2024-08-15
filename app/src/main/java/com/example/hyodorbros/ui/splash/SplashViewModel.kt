package com.example.hyodorbros.ui.splash

import com.example.hyodorbros.base.BaseViewModel
import com.example.hyodorbros.ext.uiScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    init {
        routeHome()
    }

    private fun routeHome() {
        uiScope {
            delay(DELAY_TIME)
            viewStateChanged(SplashViewState.RouteHome)
        }
    }

    companion object {
        private const val DELAY_TIME = 2000L
    }
}