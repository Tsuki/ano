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
  private var mLastCat = ""

  fun fetchData(cat: String): Disposable? {
    if (mLastCat == cat) return null
    this.mLastCat = cat
    return repository.animDetail(cat)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .subscribe {
        this.episode.value = it.article
        if (it.hasNext != "") {
          fetchNext(it.url)
        }
      }
  }

  private fun fetchNext(url: String): Disposable? {
    return repository.animDetailNext(url)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .subscribe {
        this.episode.value = this.episode.value?.plus(it.article)
        if (it.hasNext != "") {
          fetchNext(it.url)
        }
      }
  }
}
