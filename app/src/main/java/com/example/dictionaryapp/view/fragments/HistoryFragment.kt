package com.example.dictionaryapp.view.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.databinding.FragmentHistoryBinding
import com.example.dictionaryapp.view.adapters.RVAdapterHistoryFragment
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao


class HistoryFragment : Fragment() {
    private lateinit var adapterHistory: RVAdapterHistoryFragment

    private var _binding : FragmentHistoryBinding ?= null
    private val binding get() = _binding!!

    private lateinit var dbh: DatabaseHelper
    private lateinit var hisList: ArrayList<Histories>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbh = DatabaseHelper(requireContext())
        hisList = HistoriesDao().getHistory(dbh)
        // Log.e("HIS FRA", hisList.toString())
        adapterHistory = RVAdapterHistoryFragment(requireContext(), hisList) {
            val bundle = Bundle()
            bundle.putString("word", it)
            findNavController().navigate(com.example.dictionaryapp.R.id.action_historyFragment_to_homePageFragment, bundle)
        }
        binding.rvHistoryPage.adapter = adapterHistory
        binding.rvHistoryPage.setHasFixedSize(true)
        binding.rvHistoryPage.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }
}