package com.example.dictionaryapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.databinding.ActivityMainBinding
import com.example.dictionaryapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter:RVAdapter

    private var _binding : ActivityMainBinding?= null
    private lateinit var binding : ActivityMainBinding

    private val viewmodel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.refreshData("bank")
        getLiveData("bank")

        binding.rvWord.setHasFixedSize(true)
        binding.rvWord.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

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

                        adapter = RVAdapter(this, definitions)
                        binding.rvWord.adapter = adapter

                        for (k in definitions.indices) {
                            val definition = definitions[k]
                            // Log.e("DATAAA", definition.toString())
                        }
                    }
                }
            }
        })
    }
}