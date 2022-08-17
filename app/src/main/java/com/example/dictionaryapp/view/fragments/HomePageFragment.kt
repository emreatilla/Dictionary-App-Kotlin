package com.example.dictionaryapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentHomePageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomePageFragment : Fragment() {
    private var _binding : FragmentHomePageBinding ?= null
    private val binding get() = _binding!!


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


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                navBar.visibility = View.GONE
                if (query != null) {
                    Log.e("STRING", query)
                    findNavController().navigate(R.id.action_homePageFragment_to_searchPage, Bundle().apply {
                        putString("query", query)
                    })
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}