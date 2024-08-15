package com.example.hyodorbros.ui.community.board

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentBoardBinding
import com.example.hyodorbros.ext.showToast
import com.example.hyodorbros.ui.adapter.BoardAdapter
import com.example.hyodorbros.ui.adapter.BoardClickType
import com.example.hyodorbros.ui.community.board.add.BoardAddViewModel
import com.example.hyodorbros.ui.community.board.add.BoardAddViewModel.Companion.KEY_TYPE
import com.example.hyodorbros.ui.community.board.detail.BoardDetailViewModel.Companion.KEY_BOARD
import com.example.hyodorbros.ui.community.board.update.BoardUpdateViewModel
import com.example.hyodorbros.ui.community.board.update.BoardUpdateViewModel.Companion.KEY_Item
import com.example.hyodorbros.ui.community.board.update.BoardUpdateViewModel.Companion.KEY_TYPE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardFragment : BaseFragment<FragmentBoardBinding, BoardViewModel, BoardViewState>() {
    override val bindLayout: Int
        get() = R.layout.fragment_board
    override val viewModel: BoardViewModel by viewModels()

    private val boardAdapter = BoardAdapter()

    override fun initUi() {
        binding.viewModel = viewModel

        binding.rvBoard.adapter = boardAdapter

        boardAdapter.setOnBoardClickTypeListener { type ->
            when (type) {
                is BoardClickType.Delete -> {
                    viewModel.deleteBoard(type.item)
                }
                is BoardClickType.Detail -> {
                    findNavController().navigate(
                        R.id.action_frg_board_to_frg_board_detail,
                        bundleOf(KEY_BOARD to type.item)
                    )
                }
                is BoardClickType.Update -> {
                    findNavController().navigate(
                        R.id.action_frg_board_to_frg_board_update,
                        bundleOf(
                            KEY_Item to type.item,
                            BoardUpdateViewModel.KEY_TYPE to viewModel.getType
                        )
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }

    override fun onChangedViewState(viewState: BoardViewState) {
        when (viewState) {
            is BoardViewState.GetBoardList -> {
                boardAdapter.addAll(viewState.list)
            }
            is BoardViewState.GetType -> {
                with(binding.containerToolbar) {
                    toolbar.setBackgroundColor(R.color.toolbar)
                    val toTitle = when (viewState.type) {
                        "free" -> {
                            "자유 게시판"
                        }
                        "used" -> {
                            "중고거래 게시판"
                        }
                        "present" -> {
                            "선물후기 게시판"
                        }
                        else -> {
                            ""
                        }
                    }
                    title.text = toTitle
                }
            }
            is BoardViewState.Error -> {
                showToast(message = viewState.message)
            }
            is BoardViewState.RouteAdd -> {
                findNavController().navigate(
                    R.id.action_frg_board_to_frg_board_add,
                    bundleOf(BoardAddViewModel.KEY_TYPE to viewState.type)
                )
            }
        }
    }
}