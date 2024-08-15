package com.example.hyodorbros.ui.dday.add

import android.app.DatePickerDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentDDayAddBinding
import com.example.hyodorbros.ext.getSelectDayString
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DDayAddFragment : BaseFragment<FragmentDDayAddBinding, DDayAddViewModel, DDayAddViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_d_day_add
    override val viewModel: DDayAddViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "D-Day 추가하기"
                btnRight1.isVisible = true
                btnRight1.setImageResource(R.drawable.ic_dday_checked)
                btnRight1.setOnClickListener {
                    viewModel?.add()
                }
            }
        }
    }

    override fun onChangedViewState(viewState: DDayAddViewState) {
        when (viewState) {
            is DDayAddViewState.Error -> {
                showToast(message = viewState.message)
            }
            DDayAddViewState.RouteMain -> {
                findNavController().popBackStack()
            }
            DDayAddViewState.ShowCalendar -> {
                val cal = Calendar.getInstance()    //캘린더뷰 만들기
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        viewModel.inputDDayLiveData.value =
                            getSelectDayString(year, month, dayOfMonth)
                    }
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            DDayAddViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }
        }
    }

}