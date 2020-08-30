package com.axxess.cavista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(val comments: ArrayList<String>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        holder.bindItems(comments[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return comments.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(comment: String) {
            val tvComment = itemView.findViewById(R.id.tv_comment) as TextView
            tvComment.text = comment
        }
    }
}