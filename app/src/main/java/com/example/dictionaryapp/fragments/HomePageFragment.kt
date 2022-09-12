package com.example.dictionaryapp.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentHomePageBinding
import com.example.dictionaryapp.other.MyToast
import com.example.dictionaryapp.adapters.RVAdapter
import com.example.dictionaryapp.adapters.RVAdapterFavorites
import com.example.dictionaryapp.adapters.RVAdapterHistory
import com.example.dictionaryapp.db.db_favorites.DatabaseHelperFavorites
import com.example.dictionaryapp.db.db_favorites.Favorites
import com.example.dictionaryapp.db.db_favorites.FavoritesDao
import com.example.dictionaryapp.db.db_history.DatabaseHelper
import com.example.dictionaryapp.db.db_history.Histories
import com.example.dictionaryapp.db.db_history.HistoriesDao
import com.example.dictionaryapp.viewmodel.MainViewModel
import com.example.dictionaryapp.viewmodel.MainViewModelDailyWord
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomePageFragment : Fragment() {
    private lateinit var adapter: RVAdapter
    private lateinit var adapterHistory: RVAdapterHistory
    private lateinit var adapterFavorites: RVAdapterFavorites

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: MainViewModel by viewModels()
    private val viewModelDailyWord: MainViewModelDailyWord by viewModels()

    private lateinit var dbh: DatabaseHelper
    private lateinit var dbhf: DatabaseHelperFavorites
    private lateinit var hisList: ArrayList<Histories>
    private lateinit var favFavList: ArrayList<Favorites>

    private var word: String = ""
    private var def: String = ""
    private var speech: String = ""
    private var audio: String = ""

    private var dailyWord = ""
    private var dailyDef = ""
    private var dailySpeech= ""
    private var dailyAudio = ""

    var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
        dailyWord = getDailyWord()
        super.onViewCreated(view, savedInstanceState)

        dbh = DatabaseHelper(requireContext())
        dbhf = DatabaseHelperFavorites(requireContext())
        navBar.visibility = View.VISIBLE

        val hayn = arguments?.getString("word")
        if (hayn != null) {
            setValues(hayn)
        }



        val prefs = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        prefs.apply {
            val word = getString("WORD", "")

            if (word != dailyWord) {
                viewModelDailyWord.refreshData(dailyWord)
                getLiveDataDailyWord()

                if (dailyAudio == "") {
                    binding.cvListenDailyWord.visibility = View.GONE
                }
            }
        }
        binding.tvDateTime.text = getCurrentDate()
        binding.tvDailyWord.text = dailyWord
        binding.tvDailyWord.setOnClickListener {
            setValues(dailyWord)
        }
        binding.cvCopyDailyWord.setOnClickListener {
            copyClipboard(dailyWord)
        }
        binding.cvShareDailyWord.setOnClickListener {
            shareWord()
        }
        if (FavoritesDao().isInFavorite(dbhf, dailyWord) == 1) {
            binding.ivSaveDailyWord.setImageResource(R.drawable.ic_baseline_bookmark_24)
            binding.tvSaveDailyWord.text = "Unsave"
        } else {
            binding.ivSaveDailyWord.setImageResource(R.drawable.ic_bookmark)
            binding.tvSaveDailyWord.text = "Save"
        }
        binding.cvSaveDailyWord.setOnClickListener {
            reloadPage()
            if (FavoritesDao().isInFavorite(dbhf, dailyWord) == 1) {
                HistoriesDao().removeFavorites(dbh, dailyWord)
                FavoritesDao().deleteFavorites(dbhf, dailyWord)
                binding.ivSaveDailyWord.setImageResource(R.drawable.ic_bookmark)
                binding.tvSaveDailyWord.text = "Save"
            } else {
                HistoriesDao().addToFavorites(dbh, dailyWord)
                prefs.apply {
                    val word = getString("WORD", "")
                    val definition = getString("DEFINITION", "")
                    val speech = getString("SPEECH", "")
                    FavoritesDao().addDailyWordFavorites(dbhf, word.toString(), definition.toString(), speech.toString())
                }
                binding.ivSaveDailyWord.setImageResource(R.drawable.ic_baseline_bookmark_24)
                binding.tvSaveDailyWord.text = "Unsave"
            }
        }

        binding.cvListenDailyWord.setOnClickListener {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            prefs.apply {
                val audio = getString("AUDIO", "")
                try {
                    mediaPlayer!!.setDataSource(audio)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


        binding.cvShare.setOnClickListener {
            shareWord()
        }

        binding.cvCopy.setOnClickListener {
            copyClipboard(word)
        }

        binding.cvListen.setOnClickListener {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer!!.setDataSource(audio)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        hisList = HistoriesDao().getHistory(dbh)
        adapterHistory = RVAdapterHistory(requireContext(), hisList) { w ->
            setValues(w)
        }
        binding.rvHistory.adapter = adapterHistory


        favFavList = FavoritesDao().getTenFavorites(dbhf)
        adapterFavorites = RVAdapterFavorites(requireContext(), favFavList) { w ->
            setValues(w)
        }
        binding.rvFavorites.adapter = adapterFavorites

        if (adapterHistory.itemCount != 0) {
            binding.tvBlankHistory.visibility = View.GONE
        }
        if (adapterFavorites.itemCount != 0) {
            binding.tvBlankFavorites.visibility = View.GONE
        }



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.llSearch.visibility == View.VISIBLE) {
                        navBar.visibility = View.VISIBLE
                        binding.llSearch.visibility = View.GONE
                        binding.svHome.visibility = View.VISIBLE

                        reloadPage()
                    } else {
                        val dialog = BottomSheetDialog(
                            requireContext(),
                            R.style.BottomSheetDialog
                        )
                        dialog.setContentView(R.layout.sample_dialog_one)
                        val btnExit = dialog.findViewById<RelativeLayout>(R.id.rl_exit)
                        val btnDelete = dialog.findViewById<RelativeLayout>(R.id.rl_cancel)
                        btnExit?.setOnClickListener {
                            activity?.finish()
                        }
                        btnDelete?.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

            })

        binding.tvFavoritesSeeAll.setOnClickListener {
            TODO()
        }


        binding.btnBack.setOnClickListener {
            navBar.visibility = View.VISIBLE
            binding.llSearch.visibility = View.GONE
            binding.svHome.visibility = View.VISIBLE

            binding.searchView.visibility = View.VISIBLE
            binding.pBar.visibility = View.GONE

            reloadPage()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        _binding = null
    }

    private fun setValues(w: String) {
        binding.searchView.visibility = View.GONE
        binding.pBar.visibility = View.VISIBLE
        binding.tvWord.text = w
        viewmodel.refreshData(w)
        getLiveData()

        if (FavoritesDao().isInFavorite(dbhf, w) == 1) {
            binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_baseline_bookmark_24)
            binding.tvSave.text = "Unsave"
        } else {
            binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_bookmark)
            binding.tvSave.text = "Save"
        }

        binding.cvSave.setOnClickListener {
            if (FavoritesDao().isInFavorite(dbhf, w) == 1) {
                HistoriesDao().removeFavorites(dbh, w)
                FavoritesDao().deleteFavorites(dbhf, w)
                binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_bookmark)
                binding.tvSave.text = "Save"
            } else {
                HistoriesDao().addToFavorites(dbh, w)
                FavoritesDao().addFavorites(dbhf, dbh, w)
                binding.ivBookmarkSearchPage.setImageResource(R.drawable.ic_baseline_bookmark_24)
                binding.tvSave.text = "Unsave"
            }
        }
    }

    private fun copyClipboard(word: String) {
        val myClipboard =
            getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", word)
        MyToast.show(requireContext(), "Word \"$word\" is copied to the clipboard.", true, 0)
        myClipboard.setPrimaryClip(clip)
    }

    private fun shareWord() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Check out this Great app:")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    private fun reloadPage() {
        val id = findNavController().currentDestination?.id
        findNavController().popBackStack(id!!, true)
        findNavController().navigate(id)
    }


    private fun getDailyWord(): String {
        val wordList = arrayOf(
            "immune",
            "element",
            "nervous",
            "shell",
            "please",
            "cheap",
            "withdrawal",
            "wheat",
            "camp",
            "loop",
            "promote",
            "absolute",
            "garbage",
            "constitution",
            "clear",
            "bacon",
            "indoor",
            "pin",
            "criminal",
            "mutual",
            "view",
            "junior",
            "satellite",
            "pawn",
            "walk",
            "accident",
            "or",
            "employ",
            "motorcycle",
            "blonde",
            "continuation"
        ) //explicit type declaration
        return wordList[getCurrentDay().toInt() - 1]
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("dd")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        return sdf.format(Date())
    }

    @SuppressLint("CommitPrefEdits")
    fun getLiveDataDailyWord() {
        viewModelDailyWord.dictionary_data.observe(viewLifecycleOwner, Observer { data ->
            data?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let { it ->
                dailyWord = it[0].word
                dailyDef = it[0].meanings[0].definitions[0].definition
                dailySpeech = it[0].meanings[0].partOfSpeech.take(1)

                binding.cvListenDailyWord.visibility = View.GONE

                // bazen audio başta yada sonda olabiliyor
                for (i in it[0].phonetics) {
                    dailyAudio = i.audio
                    if (dailyAudio != "") {
                        binding.cvListenDailyWord.visibility = View.VISIBLE
                        break
                    } else {
                        continue
                    }
                }
                val prefs = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                val editor = prefs.edit()

                editor
                    .putString("WORD", dailyWord)
                    .putString("DEFINITION", dailyDef)
                    .putString("SPEECH", dailySpeech)
                    .putString("AUDIO", dailyAudio)
                    .apply()
            }
        })

    }


    @SuppressLint("SetTextI18n", "InflateParams")
    fun getLiveData() {
        viewmodel.dictionary_data.observe(viewLifecycleOwner, Observer { data ->
            data?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let { it ->
                word = it[0].word
                def = it[0].meanings[0].definitions[0].definition
                speech = it[0].meanings[0].partOfSpeech.take(1)

                binding.cvListen.visibility = View.GONE

                // bazen audio başta yada sonda olabiliyor
                for (i in it[0].phonetics) {
                    audio = i.audio
                    if (audio != "") {
                        binding.cvListen.visibility = View.VISIBLE
                        break
                    } else {
                        continue
                    }

                }

                // History kısmında kelime eğer bulunuyorsa başa ekleme
                try {
                    HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", "\"${speech}\"", 0)
                } catch (e: Exception) {
                    val isF = FavoritesDao().isInFavorite(dbhf, word)
                    HistoriesDao().deleteWord(dbh, word)
                    HistoriesDao().addWord(dbh, "\"${word}\"", "\"${def}\"", "\"${speech}\"", isF)
                }

                hisList = HistoriesDao().getHistory(dbh)
                adapterHistory = RVAdapterHistory(requireContext(), hisList) { w ->
                    setValues(w)
                }
                binding.rvHistory.adapter = adapterHistory

                favFavList = FavoritesDao().getTenFavorites(dbhf)
                adapterFavorites = RVAdapterFavorites(requireContext(), favFavList) { w ->
                    setValues(w)
                }
                binding.rvFavorites.adapter = adapterFavorites


                for (i in 0 until it.size) {
                    val dictionaryModelItem = it[i]

                    adapter = RVAdapter(requireContext(), it)
                    binding.rvWord.adapter = adapter

                }
            }
        })

        viewmodel.dictionary_error.observe(viewLifecycleOwner, Observer { error ->
            error?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                if (it)
                    MyToast.show(requireContext(), "You gave invalid word ! ", true, 1)
                binding.searchView.visibility = View.VISIBLE
                binding.pBar.visibility = View.GONE
            }
        })

        viewmodel.dictionary_load.observe(viewLifecycleOwner, Observer { load ->
            load?.takeIf { userVisibleHint }?.getContentIfNotHandled()?.let {
                if (it) {
                    binding.svHome.visibility = View.VISIBLE
                    binding.llSearch.visibility = View.GONE
                } else {
                    val navBar =
                        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_bar)
                    binding.svHome.visibility = View.GONE
                    binding.llSearch.visibility = View.VISIBLE
                    binding.rvWord.setHasFixedSize(true)
                    binding.rvWord.layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    navBar.visibility = View.GONE
                }
            }
        })
    }
}