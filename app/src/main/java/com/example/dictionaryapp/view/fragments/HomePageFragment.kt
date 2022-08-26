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
import com.example.dictionaryapp.view.adapters.RVAdapterFavorites
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
    private lateinit var adapterFavorites: RVAdapterFavorites

    private var _binding : FragmentHomePageBinding ?= null
    private val binding get() = _binding!!

    private val viewmodel: MainViewModel by viewModels()

    private lateinit var dbh: DatabaseHelper
    private lateinit var hisList: ArrayList<Histories>
    private lateinit var favList: ArrayList<Histories>

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

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
        super.onViewCreated(view, savedInstanceState)

        dbh = DatabaseHelper(requireContext())
        navBar.visibility = View.VISIBLE

        val hayn = arguments?.getString("word")
        if (hayn != null) {
            setValues(hayn)
            // arguments?.getString("word")?.let { Log.e("getarg", it) }
        }

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

        // HistoriesDao().addWord(dbh, "\"Hi\"", "\"Hİİ\"", 0)
        hisList = HistoriesDao().getLastTenHistory(dbh)
        adapterHistory = RVAdapterHistory(requireContext(), hisList) { w ->
            setValues(w)
        }
        binding.rvHistory.adapter = adapterHistory


        favList = HistoriesDao().getTenFavorites(dbh)
        adapterFavorites = RVAdapterFavorites(requireContext(), favList) { w ->
            setValues(w)
        }
        binding.rvFavorites.adapter = adapterFavorites


        // Log.e("DB", hisList.toString())


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.llSearch.visibility == View.VISIBLE) {
                    navBar.visibility = View.VISIBLE
                    binding.llSearch.visibility = View.GONE
                    binding.svHome.visibility = View.VISIBLE

                    val navController = findNavController()
                    val id = navController.currentDestination?.id
                    navController.popBackStack(id!!,true)
                    navController.navigate(id)

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

            val navController = findNavController()
            val id = navController.currentDestination?.id
            navController.popBackStack(id!!,true)
            navController.navigate(id)


            /*
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
            }
             */
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    setValues(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        arguments?.clear()
    }

    private fun setValues(w: String) {
        binding.searchView.visibility = View.GONE
        binding.pBar.visibility = View.VISIBLE
        binding.tvWord.text = w
        viewmodel.refreshData(w)
        getLiveData()

        if (HistoriesDao().isFavorite(dbh, w) == 1) {
            binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_baseline_bookmark_24)
            binding.tvSave.text = "Unsave"
        } else {
            binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_bookmark)
            binding.tvSave.text = "Save"
        }

        binding.cvSave.setOnClickListener {
            if (HistoriesDao().isFavorite(dbh, w) == 1) {
                HistoriesDao().removeFavorites(dbh, w)
                binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_bookmark)
                binding.tvSave.text = "Save"
            } else {
                HistoriesDao().addToFavorites(dbh, w)
                binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_baseline_bookmark_24)
                binding.tvSave.text = "Unsave"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getLiveData() {
        viewmodel.dictionary_data.observe(viewLifecycleOwner, Observer { data ->
            // viewmodel.dictionary_data.value = null
            data?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let { it ->
                word = it[0].word
                def = it[0].meanings[0].definitions[0].definition
                speech = it[0].meanings[0].partOfSpeech.take(1)

                binding.cvListen.visibility = View.GONE

                // bazen audio başta yada sonda olabiliyor
                for (i in it[0].phonetics) {
                    audio = i.audio
                    Log.e("audio", audio)
                    if (audio != "") {
                        binding.cvListen.visibility = View.VISIBLE
                        break
                    } else {
                        continue
                    }

                    // audio = it[0].phonetics[it[0].phonetics.lastIndex].audio
                }
                // Log.e("addWord", "word : $word def : $def")

                // History kısmında kelime eğer bulunuyorsa başa ekleme
                try {
                    HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", "\"${speech}\"", 0)
                } catch (e:Exception) {
                    val isF = HistoriesDao().isFavorite(dbh, word)
                    HistoriesDao().deleteWord(dbh, word)
                    HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", "\"${speech}\"", isF)
                }
                // HistoriesDao().getLastTenHistory(dbh)

                hisList = HistoriesDao().getLastTenHistory(dbh)
                adapterHistory = RVAdapterHistory(requireContext(), hisList){ w ->
                    setValues(w)
                }
                binding.rvHistory.adapter = adapterHistory

                favList = HistoriesDao().getTenFavorites(dbh)
                adapterFavorites = RVAdapterFavorites(requireContext(), favList) { w ->
                    setValues(w)
                }
                binding.rvFavorites.adapter = adapterFavorites


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