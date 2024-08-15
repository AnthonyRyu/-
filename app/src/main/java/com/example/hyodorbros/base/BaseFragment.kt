package com.example.hyodorbros.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel, S : ViewState> : Fragment() {
    protected lateinit var binding: B

    protected abstract val bindLayout: Int
    protected abstract val viewModel: V

    abstract fun initUi()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, bindLayout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initUi()

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            (viewState as? S)?.let { onChangedViewState(it) }
        }

        return binding.root
    }

    abstract fun onChangedViewState(viewState: S)

}

