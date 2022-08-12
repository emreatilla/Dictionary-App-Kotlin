package com.example.dictionaryapp.service

import com.example.dictionaryapp.model.DictionaryModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

// https://api.dictionaryapi.dev/api/v2/entries/en/bank

interface DictionaryAPI {
    @GET("/api/v2/entries/en/{word}")
    fun getData(
        @Path("word") word: String
    ): Single<DictionaryModel>
}