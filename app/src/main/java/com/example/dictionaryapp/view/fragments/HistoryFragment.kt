package com.example.dictionaryapp.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.databinding.FragmentHistoryBinding
import com.example.dictionaryapp.view.SwipeToDeleteCallback
import com.example.dictionaryapp.view.adapters.RVAdapterHistoryFragment
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao
import com.example.dictionaryapp.view.dialogs.CustomOneDialog
import com.example.dictionaryapp.viewmodel.SharedViewModel


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbh = DatabaseHelper(requireContext())

        callRecyclerView()

        if (adapterHistory.itemCount != 0) {
            binding.tvBlankHistory.visibility = View.GONE
            binding.lottieEmpty.visibility = View.GONE
            binding.ivDeleteHistory.visibility = View.VISIBLE
        }

        binding.ivDeleteHistory.setOnClickListener {
            CustomOneDialog().show(childFragmentManager, "CustomOneDialog")

            val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
            // observing the change in the message declared in SharedViewModel
            model.condition.observe(viewLifecycleOwner, Observer {
                // updating data in displayMsg
                Log.e("itttttt", it.toString())
                if (it) {
                    reloadPage()
                }
            })
            /*
            AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to permanently delete all search history ?")
                .setPositiveButton("Delete") {_, _ ->
                    HistoriesDao().deleteAllRecords(dbh)
                    reloadPage()
                }
                .setNegativeButton("Cancel") {_, _ -> }
                .show()
             */

        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Log.e("hislist", hisList[position].word)
                HistoriesDao().deleteWord(dbh, hisList[position].word)
                callRecyclerView()
                // Log.e("hislist", hisList.toString())
                binding.rvHistoryPage.adapter?.notifyItemRemoved(position)
                reloadPage()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvHistoryPage)
    }

    private fun reloadPage() {
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!, true)
        findNavController().navigate(id)
    }

    private fun callRecyclerView() {
        hisList = HistoriesDao().getHistory(dbh)
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