package com.example.dictionaryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){
    val condition = MutableLiveData<Boolean>()

    fun sendBoolean(bool: Boolean) {
        condition.value = bool
    }
}