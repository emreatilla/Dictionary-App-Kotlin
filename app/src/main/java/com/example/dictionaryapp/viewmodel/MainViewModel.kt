package com.example.dictionaryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionaryapp.model.DictionaryModel
import com.example.dictionaryapp.service.DictionaryAPIService
import com.example.dictionaryapp.other.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {
    private val dictionaryAPIService = DictionaryAPIService()
    private val disposable = CompositeDisposable()

    private val dictionary_data_ = MutableLiveData<SingleLiveEvent<DictionaryModel>>()
    val dictionary_data: LiveData<SingleLiveEvent<DictionaryModel>> get() = dictionary_data_

    private val dictionary_error_ = MutableLiveData<SingleLiveEvent<Boolean>>()
    val dictionary_error: LiveData<SingleLiveEvent<Boolean>> get() = dictionary_error_

    private val dictionary_load_ = MutableLiveData<SingleLiveEvent<Boolean>>()
    val dictionary_load: LiveData<SingleLiveEvent<Boolean>> get() = dictionary_load_

    fun refreshData(word: String) {
        getDataFromAPI(word)
    }

    private fun getDataFromAPI(word: String) {
        dictionary_load_.value = SingleLiveEvent(true)

        disposable.add(
            dictionaryAPIService.getDataService(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ deger ->

                    dictionary_data_.postValue(SingleLiveEvent(deger))
                    dictionary_error_.value = SingleLiveEvent(false)
                    dictionary_load_.value = SingleLiveEvent(false)
                },
                    {
                        dictionary_error_.value = SingleLiveEvent(true)
                        dictionary_load_.value = SingleLiveEvent(true)
                    })
        )
    }

}