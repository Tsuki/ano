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

class HistoryViewModel @Inject constructor(private val historyDao: WatchHistoryDao) : ViewModel() {
  var favorites = MutableLiveData<List<WatchHistory>>().apply { value = emptyList() }

  fun fetchData(): Disposable? {
    return this.historyDao.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .onErrorReturn { emptyList() }
      .subscribe { favorites.value = it }
  }
}
