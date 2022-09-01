package com.example.dictionaryapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentFavoritesBinding
import com.example.dictionaryapp.view.adapters.RVAdapterFavoritesFragment
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao

class FavoritesFragment : Fragment() {
    private lateinit var adapterHistory: RVAdapterFavoritesFragment

    private var _binding : FragmentFavoritesBinding?= null
    private val binding get() = _binding!!

    private lateinit var dbh: DatabaseHelper
    private lateinit var favList: ArrayList<Histories>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbh = DatabaseHelper(requireContext())
        favList = HistoriesDao().getFavorites(dbh)
        // Log.e("HIS FRA", hisList.toString())
        adapterHistory = RVAdapterFavoritesFragment(requireContext(), favList) {
            val bundle = Bundle()
            bundle.putString("word", it)
            findNavController().navigate(R.id.action_favoritesFragment_to_homePageFragment, bundle)
        }
        binding.rvFavoritesPage.adapter = adapterHistory
        binding.rvFavoritesPage.setHasFixedSize(true)
        binding.rvFavoritesPage.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        if (adapterHistory.itemCount != 0) {
            binding.tvBlankHistory.visibility = View.GONE
        }
    }
}