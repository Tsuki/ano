package com.sukitsuki.ano.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sukitsuki.ano.R
import com.sukitsuki.ano.entity.Favorite
import kotlinx.android.synthetic.main.view_anime_list_holder.view.*

class FavoriteAdapter(val context: Context) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {


  lateinit var onItemClick: ((Favorite) -> Unit)
  var dataSet: List<Favorite> = emptyList()

  fun loadDataSet(data: List<Favorite>) {
    dataSet = data
    this.notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
    val textView =
      LayoutInflater.from(parent.context).inflate(R.layout.view_anime_list_holder, parent, false)
    return ViewHolder(textView)
  }

  override fun getItemCount(): Int = dataSet.size

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
    val anim = dataSet[position]
    holder.textView.text = anim.anim.title
  }


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.animTitle

    init {
      textView.isSelected = true
      itemView.setOnClickListener {
        onItemClick.invoke(dataSet[adapterPosition])
      }
    }
  }
}
