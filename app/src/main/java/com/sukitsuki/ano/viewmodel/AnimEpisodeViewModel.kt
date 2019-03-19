package com.sukitsuki.ano.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.model.AnimEpisode
import com.sukitsuki.ano.repository.BackendRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AnimEpisodeViewModel @Inject constructor(private val repository: BackendRepository) : ViewModel() {
  var episode = MutableLiveData<List<AnimEpisode>>().apply { value = emptyList() }


  fun fetchData(cat: String): Disposable? {
    return repository.animDetail(cat)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .subscribe { this.episode.value = it.article }
  }
}
