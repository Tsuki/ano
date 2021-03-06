package com.sukitsuki.ano.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.dao.WatchHistoryDao
import com.sukitsuki.ano.entity.WatchHistory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val dao: WatchHistoryDao) : ViewModel() {
  var history = MutableLiveData<List<WatchHistory>>().apply { value = emptyList() }

  fun fetchData(): Disposable? {
    return this.dao.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .onErrorReturn { emptyList() }
      .subscribe { history.value = it }
  }

  fun deleteItem(item: WatchHistory) {
    return this.dao.deleteOne(item)
  }
}
