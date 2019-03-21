package com.sukitsuki.ano.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sukitsuki.ano.R
import com.sukitsuki.ano.model.Anim
import kotlinx.android.synthetic.main.view_anime_list_holder.view.*


class AnimListAdapter(val context: Context) : RecyclerView.Adapter<AnimListAdapter.ViewHolder>() {
  lateinit var onItemClick: ((Anim) -> Unit)
  var oriDataSet: List<Anim> = emptyList()
  var dataSet: List<Anim> = emptyList()

  fun loadDataSet(newDataSet: List<Anim>) {
    oriDataSet = newDataSet
    dataSet = oriDataSet
    this.notifyDataSetChanged()
  }

  fun filter(query: String) {
    dataSet = if (query == "") oriDataSet
    else oriDataSet.filter { it.title.toLowerCase().contains(query.toLowerCase()) }
    this.notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val textView =
      LayoutInflater.from(parent.context).inflate(R.layout.view_anime_list_holder, parent, false)
    return ViewHolder(textView)
  }

  override fun getItemCount() = dataSet.size

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val anim = dataSet[position]
    holder.textView.text = "${anim.title}-${anim.episodeTitle}"
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.animTitle

    init {
      // to enable marquee
      textView.isSelected = true
      itemView.setOnClickListener {
        onItemClick.invoke(dataSet[adapterPosition])
      }
    }
  }
}
