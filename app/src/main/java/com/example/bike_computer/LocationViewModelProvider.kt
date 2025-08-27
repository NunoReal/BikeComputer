package com.example.bike_computer

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

object LocationViewModelProvider : ViewModelStoreOwner {
    private val store = ViewModelStore()
    private val provider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())

    fun getInstance(): LocationViewModel {
        return provider[LocationViewModel::class.java]
    }

    override val viewModelStore: ViewModelStore
        get() = store
}

