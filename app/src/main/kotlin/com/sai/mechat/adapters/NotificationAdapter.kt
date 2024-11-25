package com.sai.mechat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.sai.mechat.R
import com.sai.mechat.models.NotificationModel

class NotificationAdapter(
    private val context: Context,
    private val itemList: ArrayList<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    class NotificationViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.notifictions_layout,parent,false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.textView.text = itemList.get(position).message
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}