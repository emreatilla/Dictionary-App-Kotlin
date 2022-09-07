package com.example.dictionaryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){
    // variable to contain message whenever
    // it gets changed/modified(mutable)
    val condition = MutableLiveData<Boolean>()

    // function to send message
    fun sendBoolean(bool: Boolean) {
        condition.value = bool
    }
}