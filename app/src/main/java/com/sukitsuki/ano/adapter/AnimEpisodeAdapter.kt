package com.sukitsuki.ano.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sukitsuki.ano.model.AnimEpisode
import com.sukitsuki.ano.viewmodel.AnimEpisodeViewModel


class AnimEpisodeAdapter(private val viewModel: AnimEpisodeViewModel) :
  RecyclerView.Adapter<AnimEpisodeAdapter.ViewHolder>() {
  private var mDataSet: List<AnimEpisode> = emptyList()

  fun loadDataSet(newDataSet: List<AnimEpisode>) {
    mDataSet = newDataSet
    this.notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val textView =
      LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
    return ViewHolder(textView)
  }

  private val mTransparent by lazy { Color.parseColor("#00FFFFFF") }
  private val mBlackOverlay by lazy { Color.parseColor("#66000000") }

  override fun getItemCount() = mDataSet.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (mDataSet[position] == viewModel.animEpisode.value)
      holder.itemView.setBackgroundColor(mBlackOverlay)
    else
      holder.itemView.setBackgroundColor(mTransparent)
    holder.textView.text = mDataSet[position].title
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(android.R.id.text1)

    init {
      itemView.setOnClickListener {
        if (mDataSet[adapterPosition] == viewModel.animEpisode.value)
          return@setOnClickListener
        viewModel.animEpisode.value = mDataSet[adapterPosition]
        viewModel.fetchAnimFrame(mDataSet[adapterPosition])
        notifyDataSetChanged()
      }
    }
  }
}
