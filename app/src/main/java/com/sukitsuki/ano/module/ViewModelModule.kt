package com.sukitsuki.ano.module

import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.utils.ViewModelKey
import com.sukitsuki.ano.viewmodel.AnimEpisodeViewModel
import com.sukitsuki.ano.viewmodel.HomeViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module(includes = [])
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  abstract fun bindAnimeListViewModel(viewModel: HomeViewModel): ViewModel


  @Binds
  @IntoMap
  @ViewModelKey(AnimEpisodeViewModel::class)
  abstract fun bindAnimEpisodeViewModel(viewModel: AnimEpisodeViewModel): ViewModel


}
