package com.sukitsuki.ano.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sukitsuki.ano.model.AnimEpisode
import com.sukitsuki.ano.model.AnimFrame
import com.sukitsuki.ano.repository.BackendRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class AnimEpisodeAdapter(val backendRepository: BackendRepository) :
  RecyclerView.Adapter<AnimEpisodeAdapter.ViewHolder>() {
  lateinit var onItemClick: ((AnimFrame) -> Unit)
  var dataSet: List<AnimEpisode> = emptyList()

  fun loadDataSet(newDataSet: List<AnimEpisode>) {
    dataSet = newDataSet
    this.notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val textView =
      LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
    return ViewHolder(textView)
  }

  override fun getItemCount() = dataSet.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val anim = dataSet[position]
    holder.textView.text = anim.title
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(android.R.id.text1)

    init {
      itemView.setOnClickListener {
        val url = dataSet[adapterPosition].url
        if (url == "") {
          onItemClick.invoke(AnimFrame())
          return@setOnClickListener
        }
        backendRepository.animVideo(url)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnError { Timber.w(it) }
          .subscribe { onItemClick.invoke(it) }
      }
    }
  }
}
