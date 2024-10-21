package com.sai.mechat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sai.mechat.R
import com.sai.mechat.models.User

class StoriesAdapter(context: Context,private val storiesList: ArrayList<User>) :
    RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {


    class StoriesViewHolder(itemView: View) : ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_stories,parent,false)
        return StoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storiesList.size
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {

    }
}