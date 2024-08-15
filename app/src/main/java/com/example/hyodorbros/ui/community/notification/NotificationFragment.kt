package com.example.hyodorbros.ui.community.notification

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentNotificationBinding
import com.example.hyodorbros.ui.adapter.NotificationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationViewModel, NotificationViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_notification
    override val viewModel: NotificationViewModel by viewModels()

    private val notificationAdapter = NotificationAdapter()


    override fun initUi() {
        binding.viewModel = viewModel

        with(binding) {
            with(containerToolbar) {
                toolbar.setBackgroundColor(R.color.toolbar)
                title.text = "공지 사항"
            }
            rvNotification.adapter = notificationAdapter
        }
    }

    override fun onChangedViewState(viewState: NotificationViewState) {
        when (viewState) {
            NotificationViewState.EmptyNotification -> {
                with(binding) {
                    emptyNoti.isVisible = true
                    rvNotification.isVisible = false
                }
            }
            is NotificationViewState.GetNotificationList -> {
                with(binding) {
                    emptyNoti.isVisible = false
                    rvNotification.isVisible = true
                }
                notificationAdapter.addAll(viewState.list)
            }
        }
    }
}