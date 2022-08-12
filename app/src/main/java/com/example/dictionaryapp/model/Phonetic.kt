package com.example.dictionaryapp.model

data class Phonetic(
    val audio: String,
    val license: License,
    val sourceUrl: String,
    val text: String
)