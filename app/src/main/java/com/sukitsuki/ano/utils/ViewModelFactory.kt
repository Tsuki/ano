package com.sukitsuki.ano.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val viewModelsMap: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
  ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val creator = viewModelsMap[modelClass] ?: viewModelsMap.asIterable().firstOrNull {
      modelClass.isAssignableFrom(it.key)
    }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
    return try {
      @Suppress("UNCHECKED_CAST")
      creator.get() as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

}
