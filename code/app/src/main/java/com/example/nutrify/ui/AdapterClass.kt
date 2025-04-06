package com.example.nutrify.ui

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nutrify.R

class AdapterClass(private val questionList: ArrayList<QuestionAnswer>): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    var onItemClick: ((QuestionAnswer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.questionhistory_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = questionList[position]
        holder.questionItem.text = currentItem.question

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val questionItem:TextView = itemView.findViewById(R.id.questionhistoryelement)
    }
}