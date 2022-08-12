package com.example.dictionaryapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dictionaryapp.databinding.ActivityMainBinding
import com.example.dictionaryapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding?= null
    private lateinit var binding : ActivityMainBinding

    private lateinit var viewmodel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewmodel.refreshData("bank")
        getLiveData("bank")
    }

    private fun getLiveData(word: String) {
        viewmodel.dictionary_data.observe(this, Observer { data ->
            data?.let {
                for (i in 0 until data.size) {
                    val dictionaryModelItem = data[i]
                    val meanings = dictionaryModelItem.meanings

                    for (j in meanings.indices) {
                        val meaning = meanings[j]
                        val definitions = meaning.definitions

                        for (k in definitions.indices) {
                            val definition = definitions[k]
                            Log.e("DATAAA", definition.toString())
                        }
                    }
                }
            }
        })
    }
}