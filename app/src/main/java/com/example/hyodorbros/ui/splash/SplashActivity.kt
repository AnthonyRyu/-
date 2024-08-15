package com.example.hyodorbros.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseActivity
import com.example.hyodorbros.databinding.ActivitySplashBinding
import com.example.hyodorbros.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel, SplashViewState>() {

    override val bindLayout: Int
        get() = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModels()

    override fun initUi() {}

    override fun onChangedViewState(viewState: SplashViewState) {
        when (viewState) {
            SplashViewState.RouteHome -> {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            }
        }
    }
}