package com.sukitsuki.ano.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.model.AnimEpisode
import com.sukitsuki.ano.model.AnimFrame
import com.sukitsuki.ano.repository.BackendRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AnimEpisodeViewModel @Inject constructor(private val repository: BackendRepository) : ViewModel() {
  val episode = MutableLiveData<List<AnimEpisode>>().apply { value = emptyList() }
  val animEpisode = MutableLiveData<AnimEpisode>()
  val animFrame = MutableLiveData<AnimFrame>()
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

  fun fetchAnimFrame(episode: AnimEpisode): Disposable? {
    if (episode.url == "") return null
    return repository.animVideo(episode.url)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Timber.w(it) }
      .onErrorReturn { AnimFrame() }
      .subscribe { animFrame.value = it }
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
