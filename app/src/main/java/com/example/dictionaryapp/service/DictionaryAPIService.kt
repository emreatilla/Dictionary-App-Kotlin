package com.example.dictionaryapp.service

import com.example.dictionaryapp.model.DictionaryModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class DictionaryAPIService {
    private val BASE_URL = "https://api.dictionaryapi.dev"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DictionaryAPI::class.java)

    fun getDataService(word: String): Single<DictionaryModel>{
        return api.getData(word)
    }

}