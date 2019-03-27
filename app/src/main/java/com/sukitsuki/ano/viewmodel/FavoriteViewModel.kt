package com.sukitsuki.ano.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.dao.FavoriteDao
import com.sukitsuki.ano.entity.Favorite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favoriteDao: FavoriteDao) : ViewModel() {

  var favorites = MutableLiveData<List<Favorite>>().apply { value = emptyList() }

  fun fetchData(): Disposable? {
    return this.favoriteDao.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .onErrorReturn { emptyList() }
      .subscribe { favorites.value = it }
  }
}
