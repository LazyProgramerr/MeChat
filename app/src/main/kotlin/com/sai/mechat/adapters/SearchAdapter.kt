package com.sai.mechat.adapters

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sai.mechat.R
import com.sai.mechat.activities.SearchActivity
import com.sai.mechat.fragments.MirrorFragment
import com.sai.mechat.models.User

class SearchAdapter (private val itemList: ArrayList<User>,val str: String,val context: Context): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_user,parent,false)
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val userName = itemList.get(position).userName
        val spannableString = SpannableString(userName)
        val startIndex = userName?.indexOf(str, ignoreCase = true)

        if (startIndex != null && startIndex >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(Color.YELLOW), // Highlight color
                startIndex,
                startIndex + str.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        holder.textView.text = spannableString

        holder.textView.setOnClickListener {
            Toast.makeText(context,itemList.get(position).userName,Toast.LENGTH_SHORT).show()
            val fragment = MirrorFragment.newInstance(itemList.get(position).uid)
            (context as SearchActivity).showFragment(fragment)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class SearchViewHolder(itemView: View) : ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.user_name)
    }
}