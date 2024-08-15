package com.example.hyodorbros.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel, S : ViewState> :
    AppCompatActivity() {

    protected lateinit var binding: B

    protected abstract val bindLayout: Int
    protected abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this, bindLayout)
        setContentView(binding.root)

        viewModel.viewStateLiveData.observe(this) { viewState ->
            (viewState as? S)?.let { onChangedViewState(it) }
        }

        initUi()
    }

    abstract fun initUi()

    abstract fun onChangedViewState(viewState: S)

}