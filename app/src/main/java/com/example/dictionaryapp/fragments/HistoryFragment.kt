package com.example.dictionaryapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentHistoryBinding
import com.example.dictionaryapp.other.SwipeToDeleteCallback
import com.example.dictionaryapp.adapters.RVAdapterHistoryFragment
import com.example.dictionaryapp.db.db_history.DatabaseHelper
import com.example.dictionaryapp.db.db_history.Histories
import com.example.dictionaryapp.db.db_history.HistoriesDao
import com.example.dictionaryapp.dialogs.CustomOneDialog
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
                if (it) {
                    callRecyclerView()
                    binding.tvBlankHistory.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.VISIBLE
                    binding.ivDeleteHistory.visibility = View.GONE
                    model.sendBoolean(false)
                }
            })
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                HistoriesDao().deleteWord(dbh, hisList[position].word)
                callRecyclerView()
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
            findNavController().navigate(R.id.action_historyFragment_to_homePageFragment, bundle)
        }
        binding.rvHistoryPage.adapter = adapterHistory
        binding.rvHistoryPage.setHasFixedSize(true)
        binding.rvHistoryPage.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }
}