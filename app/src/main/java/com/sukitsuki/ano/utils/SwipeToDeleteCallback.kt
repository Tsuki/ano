package com.sukitsuki.ano.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeToDeleteCallback(private val mAdapter: SwipeToDeleteCallbackAdapter) :
  ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*or ItemTouchHelper.RIGHT*/) {
  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ): Boolean {
    return false
  }


  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    val position = viewHolder.adapterPosition
    mAdapter.deleteItem(position)
  }
}

interface SwipeToDeleteCallbackAdapter {
  fun deleteItem(position: Int)
  var deleteItemFun: (Any) -> Unit
}
