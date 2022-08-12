package com.example.dictionaryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionaryapp.model.DictionaryModel
import com.example.dictionaryapp.service.DictionaryAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {
    private val dictionaryAPIService = DictionaryAPIService()
    private val disposable = CompositeDisposable()

    val dictionary_data = MutableLiveData<DictionaryModel>()
    val dictionary_error = MutableLiveData<Boolean>()
    val dictionary_load = MutableLiveData<Boolean>()

    fun refreshData(word: String) {
        getDataFromAPI(word)
    }

    private fun getDataFromAPI(word: String) {
        dictionary_load.value = true
        disposable.add(
            dictionaryAPIService.getDataService(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DictionaryModel>(){
                    override fun onSuccess(t: DictionaryModel) {
                        dictionary_data.value = t
                        dictionary_error.value = false
                        dictionary_load.value = false
                    }

                    override fun onError(e: Throwable) {
                        dictionary_error.value = true
                        dictionary_load.value = true
                    }

                })
        )
    }
}