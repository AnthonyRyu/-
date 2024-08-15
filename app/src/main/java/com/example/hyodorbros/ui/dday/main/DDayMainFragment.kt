package com.example.hyodorbros.ui.dday.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.constant.Constant
import com.example.hyodorbros.databinding.FragmentDDayMainBinding
import com.example.hyodorbros.ext.showToast
import com.example.hyodorbros.service.NotificationService
import com.example.hyodorbros.ui.adapter.DDayAdapter
import com.example.hyodorbros.ui.adapter.DDayClickEvent
import com.example.hyodorbros.ui.dday.update.DDayUpdateViewModel.Companion.KEY_ITEM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DDayMainFragment :
    BaseFragment<FragmentDDayMainBinding, DDayMainViewModel, DDayMainViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_d_day_main
    override val viewModel: DDayMainViewModel by viewModels()

    private val dDayAdapter = DDayAdapter()

    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "효도르 부탁해"
                btnLeft.isVisible = true
                btnLeft.setImageResource(R.drawable.ic_menu)

                btnLeft.setOnClickListener {
                    requireActivity().findViewById<DrawerLayout>(R.id.container_drawer).openDrawer(
                        GravityCompat.START
                    )
                }
            }
            rvDDay.adapter = dDayAdapter
        }

        dDayAdapter.setOnDDayClickEventListener { event ->
            when (event) {
                is DDayClickEvent.Delete -> {
                    viewModel.delete(event.item.entity)
                }
                is DDayClickEvent.Expand -> {
                    dDayAdapter.toggleExpand(event.item)
                }
                is DDayClickEvent.Update -> {
                    viewModel.update(event.item.entity)
                }

                is DDayClickEvent.ToggleNotification -> {
                    viewModel.toggleNotification(event.item.entity)
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.uiStateStream.collect { result ->
                binding.containerNoDDay.isVisible = result.totalCount == 0
                binding.rvDDay.isVisible = result.totalCount != 0
                result.list?.let { dDayAdapter.addAll(it) }
            }
        }
    }

    override fun onChangedViewState(viewState: DDayMainViewState) {
        when (viewState) {
            DDayMainViewState.RouteAdd -> {
                findNavController().navigate(R.id.action_frg_main_to_frg_add)
            }
            is DDayMainViewState.RouteUpdate -> {
                findNavController().navigate(
                    R.id.action_frg_main_to_frg_update,
                    bundleOf(KEY_ITEM to viewState.item.uid)
                )
            }
            is DDayMainViewState.HideNotification -> {
                val mNotificationManager =
                    requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.cancel(viewState.item.uid)
                showToast(message = getString(R.string.snackbar_msg_add_dday))
            }
            is DDayMainViewState.ShowNotification -> {
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
        }
    }

}