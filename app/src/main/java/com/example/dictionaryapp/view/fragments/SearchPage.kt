package com.example.dictionaryapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentSearchPageBinding
import com.example.dictionaryapp.view.adapters.RVAdapter
import com.example.dictionaryapp.viewmodel.MainViewModel


class SearchPage : Fragment() {
    private lateinit var adapter: RVAdapter

    private var _binding : FragmentSearchPageBinding ?= null
    private val binding get() = _binding!!

    private val viewmodel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvWord.setHasFixedSize(true)
        binding.rvWord.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val dicWord = requireArguments().getString("query")
        binding.tvWord.text = dicWord
        if (dicWord != null) {
            viewmodel.refreshData(dicWord)
            getLiveData()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_searchPage_to_homePageFragment)
        }
    }

    private fun getLiveData() {
        viewmodel.dictionary_data.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                for (i in 0 until data.size) {
                    val dictionaryModelItem = data[i]
                    val meanings = dictionaryModelItem.meanings
                    // Log.e("DMI", dictionaryModelItem.toString())

                    adapter = RVAdapter(requireContext(), data)
                    binding.rvWord.adapter = adapter

                    for (j in meanings.indices) {
                        val meaning = meanings[j]
                        val definitions = meaning.definitions
                        // Log.e("MEANING", meaning.toString())

                        // adapter = RVAdapter(this, definitions)
                        // binding.rvWord.adapter = adapter

                        for (k in definitions.indices) {
                            val definition = definitions[k]
                            // Log.e("DEFINITION", definition.toString())
                        }
                    }
                }
            }
        })

        viewmodel.dictionary_error.observe(viewLifecycleOwner, Observer{ error ->
            error?.let {
                if (error) {
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}