package com.sukitsuki.ano.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.sukitsuki.ano.R
import com.sukitsuki.ano.dao.FavoriteDao
import com.sukitsuki.ano.viewmodel.FavoriteViewModel
import dagger.android.AndroidInjector
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class FavoriteFragment : DaggerFragment() {

  companion object {
    fun newInstance() = FavoriteFragment()
  }

  @dagger.Subcomponent(modules = [])
  interface Component : AndroidInjector<FavoriteFragment> {
    @dagger.Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FavoriteFragment>()
  }

  @Inject
  lateinit var favoriteDao: FavoriteDao
  private val compositeDisposable by lazy { CompositeDisposable() }

  private lateinit var viewModel: FavoriteViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.favorite_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
    this.favoriteDao.getAll()
      .onErrorReturn { emptyList() }
      .subscribe { Timber.d("onActivityCreated: $it") }
      .addTo(compositeDisposable)
    // TODO: Use the ViewModel
  }

  override fun onDestroyView() {
    super.onDestroyView()
    compositeDisposable.dispose()
  }
}
