package com.sukitsuki.ano.module

import androidx.lifecycle.ViewModel
import com.sukitsuki.ano.fragment.HomeViewModel
import com.sukitsuki.ano.utils.ViewModelKey
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module(includes = [])
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  abstract fun bindAnimeListViewModel(viewModel: HomeViewModel): ViewModel


}
