package com.example.hyodorbros.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.hyodorbros.R
import com.example.hyodorbros.databinding.ItemBoardBinding
import com.example.hyodorbros.ui.community.board.BoardItem
import java.text.SimpleDateFormat

class BoardAdapter : RecyclerView.Adapter<BoardViewHolder>() {

    private val boardList = mutableListOf<BoardItem>()

    private lateinit var boardClickType: (BoardClickType) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(boardList[position], boardClickType)
    }

    override fun getItemCount(): Int =
        boardList.size

    fun setOnBoardClickTypeListener(listener: (BoardClickType) -> Unit) {
        boardClickType = listener
    }

    fun addAll(list: List<BoardItem>) {
        boardList.clear()
        boardList.addAll(list)
        notifyDataSetChanged()
    }
}

class BoardViewHolder(private val binding: ItemBoardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BoardItem, boardClickType: (BoardClickType) -> Unit) {
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        binding.time.text = sdf.format(item.time.toLong())
        binding.titleTextView.text = item.title

        val popupMenu = PopupMenu(itemView.context, binding.menu)

        popupMenu.setOnMenuItemClickListener { type ->
            when (type.itemId) {
                R.id.modify -> {
                    boardClickType(BoardClickType.Update(item))
                    true
                }
                R.id.delete -> {
                    boardClickType(BoardClickType.Delete(item))
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.post)

        binding.menu.setOnClickListener {
            popupMenu.show()
        }

        binding.titleTextView.setOnClickListener {
            boardClickType(BoardClickType.Detail(item))
        }
    }
}

sealed interface BoardClickType {
    data class Delete(val item: BoardItem) : BoardClickType
    data class Update(val item: BoardItem) : BoardClickType
    data class Detail(val item: BoardItem) : BoardClickType
}