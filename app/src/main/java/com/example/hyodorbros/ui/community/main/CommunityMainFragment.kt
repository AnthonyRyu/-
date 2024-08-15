package com.example.hyodorbros.ui.community.main

import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentCommunityMainBinding
import com.example.hyodorbros.ext.showToast
import com.example.hyodorbros.ui.community.board.BoardViewModel.Companion.KEY_BOARD_TYPE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommunityMainFragment :
    BaseFragment<FragmentCommunityMainBinding, CommunityMainViewModel, CommunityMainViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_community_main
    override val viewModel: CommunityMainViewModel by viewModels()

    override fun initUi() {
        binding.viewModel = viewModel
    }

    override fun onChangedViewState(viewState: CommunityMainViewState) {
        when (viewState) {
            is CommunityMainViewState.Error -> {
                showToast(message = viewState.message)
            }
            CommunityMainViewState.Logout -> {
                showToast(message = "로그아웃 되었습니다")
                findNavController().popBackStack()
            }
            CommunityMainViewState.RouteHomePage -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.du.ac.kr/KR/index.do")
                    )
                )
            }
            CommunityMainViewState.RouteNotification -> {
                findNavController().navigate(R.id.action_frg_main_to_frg_notification)
            }
            CommunityMainViewState.RouteQuestion -> {
                findNavController().navigate(R.id.action_frg_main_to_frg_question)
            }
            CommunityMainViewState.RouteReport -> {
                findNavController().navigate(R.id.action_frg_main_to_frg_report)
            }


            CommunityMainViewState.WithDraw -> {
                showToast(message = "회원탈퇴 되었습니다")
                findNavController().popBackStack()
            }
            is CommunityMainViewState.RouteBoard -> {
                findNavController().navigate(
                    R.id.action_frg_main_to_frg_board,
                    bundleOf(KEY_BOARD_TYPE to viewState.type)
                )
            }
        }
    }
}