package com.example.hyodorbros.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyodorbros.databinding.ItemDdayBinding
import com.example.hyodorbros.ext.getDiffDays
import com.example.hyodorbros.room.entity.DDayEntity

class DDayAdapter : RecyclerView.Adapter<DDayViewHolder>() {

    private val ddayList = mutableListOf<DDayItem>()

    private lateinit var onClick: (DDayClickEvent) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DDayViewHolder {
        val binding = ItemDdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DDayViewHolder, position: Int) {
        holder.bind(ddayList[position], onClick)
    }

    override fun getItemCount(): Int =
        ddayList.size


    fun addAll(list: List<DDayEntity>) {
        ddayList.clear()
        val toDDayItemList = list.map { DDayItem(it) }
        ddayList.addAll(toDDayItemList)
        notifyDataSetChanged()
    }

    fun toggleExpand(item: DDayItem) {
        if (ddayList.contains(item)) {
            val index = ddayList.indexOf(item)
            ddayList[index].isExpand = !ddayList[index].isExpand
            notifyItemChanged(index)
        }
    }

    fun setOnDDayClickEventListener(listener: (DDayClickEvent) -> Unit) {
        onClick = listener
    }
}

data class DDayItem(
    val entity: DDayEntity,
    var isExpand: Boolean = false
)


class DDayViewHolder(private val binding: ItemDdayBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DDayItem, onClick: (DDayClickEvent) -> Unit) {
        binding.item = item

        binding.tvDay.text = itemView.context.getDiffDays(item.entity.date)

        binding.containerDetail.setOnClickListener {
            onClick(DDayClickEvent.Expand(item))
        }

        binding.delete.setOnClickListener {
            onClick(DDayClickEvent.Delete(item))
        }

        binding.update.setOnClickListener {
            onClick(DDayClickEvent.Update(item))
        }

        binding.isNoti.setOnClickListener {
            onClick(DDayClickEvent.ToggleNotification(item))
        }
    }
}

sealed interface DDayClickEvent {
    data class Update(val item: DDayItem) : DDayClickEvent
    data class Delete(val item: DDayItem) : DDayClickEvent
    data class Expand(val item: DDayItem) : DDayClickEvent
    data class ToggleNotification(val item: DDayItem) : DDayClickEvent
}


