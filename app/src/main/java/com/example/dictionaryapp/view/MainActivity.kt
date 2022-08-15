package com.example.dictionaryapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.databinding.ActivityMainBinding
import com.example.dictionaryapp.databinding.WordCardBinding
import com.example.dictionaryapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter:RVAdapter
    private lateinit var adapterMeaning:RVAdapterMeaning

    private var _binding : ActivityMainBinding?= null
    private lateinit var binding : ActivityMainBinding

    private val viewmodel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dicWord = "bank"
        viewmodel.refreshData(dicWord)
        getLiveData()

        binding.rvWord.setHasFixedSize(true)
        binding.rvWord.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getLiveData() {
        viewmodel.dictionary_data.observe(this, Observer { data ->
            data?.let {
                for (i in 0 until data.size) {
                    val dictionaryModelItem = data[i]
                    val meanings = dictionaryModelItem.meanings
                    Log.e("DMI", dictionaryModelItem.toString())

                    adapter = RVAdapter(this, data)
                    binding.rvWord.adapter = adapter

                    for (j in meanings.indices) {
                        val meaning = meanings[j]
                        val definitions = meaning.definitions
                        Log.e("MEANING", meaning.toString())

                        // adapter = RVAdapter(this, definitions)
                        // binding.rvWord.adapter = adapter

                        for (k in definitions.indices) {
                            val definition = definitions[k]
                            Log.e("DEFINITION", definition.toString())
                        }
                    }
                }
            }
        })
    }
}