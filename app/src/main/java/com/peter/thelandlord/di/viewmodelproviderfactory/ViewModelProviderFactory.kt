package com.peter.thelandlord.di.viewmodelproviderfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory constructor(
    var providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = providers[modelClass as Class<out ViewModel>] ?:
                      providers.asIterable().firstOrNull {
                          modelClass.isAssignableFrom(it.key)
                      }?.value ?:
                      throw IllegalArgumentException("unknown model class $modelClass")

        return try {
            creator.get() as T
        }catch (e: Exception){
            println("failed to find viewmodel: ${e.message}")
            throw (RuntimeException(e))
        }
    }

    override fun toString(): String {
        return "My VM Factory Created"
    }
}