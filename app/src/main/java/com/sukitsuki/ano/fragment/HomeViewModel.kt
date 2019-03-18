package com.sukitsuki.ano.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.model.Anim
import com.sukitsuki.ano.repository.BackendRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: BackendRepository) : ViewModel() {
  var favorites = MutableLiveData<List<Anim>>().apply { value = emptyList() }

  init {
    fetchData()
  }

  fun fetchData(): Disposable? {
    return repository.index()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .subscribe { this.favorites.value = it.list }
  }
}
