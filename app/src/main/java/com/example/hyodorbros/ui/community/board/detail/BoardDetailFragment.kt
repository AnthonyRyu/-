package com.example.hyodorbros.ui.community.board.detail

import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.hyodorbros.R
import com.example.hyodorbros.base.BaseFragment
import com.example.hyodorbros.databinding.FragmentBoardDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class BoardDetailFragment :
    BaseFragment<FragmentBoardDetailBinding, BoardDetailViewModel, BoardDetailViewState>() {

    override val bindLayout: Int
        get() = R.layout.fragment_board_detail
    override val viewModel: BoardDetailViewModel by viewModels()

    override fun initUi() {}

    override fun onChangedViewState(viewState: BoardDetailViewState) {
        when (viewState) {
            is BoardDetailViewState.GetItem -> {

                val sdf = SimpleDateFormat("yyyy/MM/dd")

                with(binding) {
                    with(containerToolbar) {
                        toolbar.setBackgroundColor(R.color.toolbar)
                        title.text = viewState.item.title
                    }
                    time.text = sdf.format(viewState.item.time.toLong())
                    content.text = viewState.item.content
                }

                if (viewState.item.image.isNotEmpty()) {
                    Glide.with(this).load(viewState.item.image).into(binding.image)
                }
            }
        }
    }
}