package com.example.hyodorbros.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyodorbros.databinding.ItemNotificationBinding
import com.example.hyodorbros.ui.community.notification.NotificationItem
import java.text.SimpleDateFormat

class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {

    private val notificationList = mutableListOf<NotificationItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int =
        notificationList.size

    fun addAll(list: List<NotificationItem>) {
        notificationList.clear()
        notificationList.addAll(list)
        notifyDataSetChanged()
    }
}

class NotificationViewHolder(private val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NotificationItem) {

        val sdf = SimpleDateFormat("yyyy/MM/dd")

        with(binding) {
            title.text = item.title
            content.text = item.content
            time.text = sdf.format(item.time.toLong())
        }
    }
}