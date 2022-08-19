package com.example.dictionaryapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentHomePageBinding
import com.example.dictionaryapp.model.DictionaryModel
import com.example.dictionaryapp.view.adapters.RVAdapter
import com.example.dictionaryapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomePageFragment : Fragment() {
    private lateinit var adapter: RVAdapter

    private var _binding : FragmentHomePageBinding ?= null
    private val binding get() = _binding!!

    private val viewmodel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
        super.onViewCreated(view, savedInstanceState)
        navBar.visibility = View.VISIBLE

        binding.tvFavoritesSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_favoritesFragment)
        }

        binding.tvLastSearchSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_historyFragment)
        }

        binding.btnBack.setOnClickListener {
            navBar.visibility = View.VISIBLE
            binding.llSearch.visibility = View.GONE
            binding.svHome.visibility = View.VISIBLE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.tvWord.text = query
                    viewmodel.refreshData(query)
                    getLiveData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getLiveData() {
        Log.e("--- ", "duplicate deneme")
        viewmodel.kelime.observe(viewLifecycleOwner, Observer { data ->
            // viewmodel.dictionary_data.value = null
            data?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                for (i in 0 until it.size) {
                    val dictionaryModelItem = it[i]
                    val meanings = dictionaryModelItem.meanings
                    // Log.e("DMI", dictionaryModelItem.toString())

                    adapter = RVAdapter(requireContext(), it)
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
            error?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                if (it)
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        viewmodel.dictionary_load.observe(viewLifecycleOwner, Observer{ load ->
            load?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                if (it) {
                    binding.svHome.visibility = View.VISIBLE
                    binding.llSearch.visibility = View.GONE
                } else {
                    val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
                    binding.svHome.visibility = View.GONE
                    binding.llSearch.visibility = View.VISIBLE
                    binding.rvWord.setHasFixedSize(true)
                    binding.rvWord.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    navBar.visibility = View.GONE
                }
            }
        })


    }
}