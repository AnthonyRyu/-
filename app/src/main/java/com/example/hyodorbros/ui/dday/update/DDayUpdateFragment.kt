package com.example.hyodorbros.ui.dday.update

import android.app.DatePickerDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.constant.Constant
import com.example.hyodorbros.databinding.FragmentDDayUpdateBinding
import com.example.hyodorbros.ext.getSelectDayString
import com.example.hyodorbros.ext.hideKeyboard
import com.example.hyodorbros.ext.showToast
import com.example.hyodorbros.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DDayUpdateFragment :
    BaseFragment<FragmentDDayUpdateBinding, DDayUpdateViewModel, DDayUpdateViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_d_day_update
    override val viewModel: DDayUpdateViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "D-Day 수정하기"
                btnRight1.isVisible = true
                btnRight1.setImageResource(R.drawable.ic_dday_checked)
                btnRight1.setOnClickListener {
                    viewModel?.update()
                }
            }
        }
    }

    override fun onChangedViewState(viewState: DDayUpdateViewState) {
        when (viewState) {
            is DDayUpdateViewState.Error -> {
                showToast(message = viewState.message)
            }
            DDayUpdateViewState.RouteMain -> {
                findNavController().popBackStack()
            }
            DDayUpdateViewState.ShowCalendar -> {
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
            DDayUpdateViewState.HideInputTextFiled -> {
                binding.root.hideKeyboard()
            }

            is DDayUpdateViewState.ShowNotification -> {
                val intent = Intent(requireContext(), NotificationService::class.java)
                intent.putExtra(
                    Constant.INTENT.EXTRA.KEY.SQLITE_TABLE_CLT_DDAY_ROWID,
                    viewState.item.uid
                ) //전달할 값

                intent.putExtra(
                    Constant.INTENT.EXTRA.KEY.SQLITE_TABLE_CLT_DDAY_ITEM,
                    viewState.item
                ) //전달할 값
                requireContext().startService(intent)
                showToast(message = getString(R.string.snackbar_msg_add_dday))
            }

            is DDayUpdateViewState.HideNotification -> {
                val mNotificationManager =
                    requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.cancel(viewState.item.uid)
                showToast(message = getString(R.string.snackbar_msg_add_dday))
            }
        }
    }
}