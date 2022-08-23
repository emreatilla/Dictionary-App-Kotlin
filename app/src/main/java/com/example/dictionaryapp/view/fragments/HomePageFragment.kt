package com.example.dictionaryapp.view.fragments

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentHomePageBinding
import com.example.dictionaryapp.view.adapters.RVAdapter
import com.example.dictionaryapp.view.adapters.RVAdapterHistory
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao
import com.example.dictionaryapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException


class HomePageFragment : Fragment() {
    private lateinit var adapter: RVAdapter
    private lateinit var adapterHistory: RVAdapterHistory

    private var _binding : FragmentHomePageBinding ?= null
    private val binding get() = _binding!!

    private val viewmodel: MainViewModel by viewModels()

    private lateinit var dbh: DatabaseHelper
    private lateinit var hisList: ArrayList<Histories>

    private var word: String = ""
    private var def: String = ""
    private var speech: String = ""
    private var audio: String = ""

    var mediaPlayer : MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        /*
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (binding.llSearch.visibility == View.VISIBLE) {
                binding.llSearch.visibility = View.GONE
                binding.svHome.visibility = View.VISIBLE
            } else {
                val activity = MainActivity()
                finishAffinity(activity)
                exitProcess(0);
                // Toast.makeText(requireContext(), "back tıklandı", Toast.LENGTH_SHORT).show()
            }
        }
         */
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
        super.onViewCreated(view, savedInstanceState)

        binding.cvListen.setOnClickListener {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer!!.setDataSource(audio)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            } catch (e : IOException) {
                e.printStackTrace()
            }

        }

        dbh = DatabaseHelper(requireContext())
        // HistoriesDao().addWord(dbh, "\"Hi\"", "\"Hİİ\"", 0)
        hisList = HistoriesDao().getHistory(dbh)
        adapterHistory = RVAdapterHistory(requireContext(), hisList)
        binding.rvHistory.adapter = adapterHistory


        // Log.e("DB", hisList.toString())


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.llSearch.visibility == View.VISIBLE) {
                    binding.llSearch.visibility = View.GONE
                    binding.svHome.visibility = View.VISIBLE
                } else {
                    /*
                    AlertDialog.Builder(requireContext()).setMessage("Are you sure ?")
                        .setPositiveButton("Yes") {_, _ -> activity?.finish()}
                        .setNegativeButton("No") {_, _ -> }
                        .show()
                     */
                    val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog) // Style here
                    dialog.setContentView(R.layout.sample_dialog_one)
                    val btnExit= dialog.findViewById<RelativeLayout>(R.id.rl_exit)
                    val btnDelete= dialog.findViewById<RelativeLayout>(R.id.rl_cancel)
                    btnExit?.setOnClickListener {
                        activity?.finish()
                        // Toast.makeText(requireContext(), "Clicked on Exit", Toast.LENGTH_SHORT).show()
                    }
                    btnDelete?.setOnClickListener {
                        dialog.dismiss()
                        // Toast.makeText(requireContext(), "Clicked on Cancel", Toast.LENGTH_SHORT).show()
                    }
                    dialog.show()
                }
            }

        })
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

            binding.searchView.visibility = View.VISIBLE
            binding.pBar.visibility = View.GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.searchView.visibility = View.GONE
                    binding.pBar.visibility = View.VISIBLE
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
        viewmodel.dictionary_data.observe(viewLifecycleOwner, Observer { data ->
            // viewmodel.dictionary_data.value = null
            data?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                word = it[0].word
                def = it[0].meanings[0].definitions[0].definition
                speech = it[0].meanings[0].partOfSpeech.take(1)
                audio = it[0].phonetics[0].audio
                // Log.e("addWord", "word : $word def : $def")
                try {
                    HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", "\"${speech}\"", 0)
                } catch (e:Exception) {
                }
                HistoriesDao().getHistory(dbh)

                hisList = HistoriesDao().getHistory(dbh)
                adapterHistory = RVAdapterHistory(requireContext(), hisList)
                binding.rvHistory.adapter = adapterHistory
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
                    Toast.makeText(requireContext(), "ERROR INVALID WORD", Toast.LENGTH_SHORT).show()
                    binding.searchView.visibility = View.VISIBLE
                    binding.pBar.visibility = View.GONE
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

                    // Log.e("addWord", "word : $word def : $def")
                    // HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", 0)
                    // HistoriesDao().getHistory(dbh)
                }
            }
        })


    }
}