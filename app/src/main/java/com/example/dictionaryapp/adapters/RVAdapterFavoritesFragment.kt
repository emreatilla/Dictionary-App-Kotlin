package com.example.dictionaryapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.db.db_favorites.DatabaseHelperFavorites
import com.example.dictionaryapp.db.db_favorites.Favorites
import com.example.dictionaryapp.db.db_favorites.FavoritesDao
import com.example.dictionaryapp.db.db_history.DatabaseHelper
import com.example.dictionaryapp.db.db_history.HistoriesDao

class RVAdapterFavoritesFragment (private val mContext: Context, private var historyList:List<Favorites>, val listener: (String) -> Unit) : RecyclerView.Adapter<RVAdapterFavoritesFragment.CardDesignObjectsHolder>() {

    private lateinit var dbh: DatabaseHelper
    private lateinit var dbhf: DatabaseHelperFavorites

    inner class CardDesignObjectsHolder(view: View):RecyclerView.ViewHolder(view) {
        var textViewWord:TextView
        var textViewSpeech:TextView
        var textViewDefinition:TextView
        var imageViewBookmark: ImageView
        var linearLayoutDesign: LinearLayout

        init {
            textViewWord = view.findViewById(R.id.tv_word)
            textViewSpeech = view.findViewById(R.id.tv_speech)
            textViewDefinition = view.findViewById(R.id.tv_definition)
            imageViewBookmark = view.findViewById(R.id.iv_bookmark)
            linearLayoutDesign = view.findViewById(R.id.ll_history_design)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.history_design, parent, false)
        return CardDesignObjectsHolder(design)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val history = historyList[position]

        dbh = DatabaseHelper(mContext)
        dbhf = DatabaseHelperFavorites(mContext)

        holder.imageViewBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)

        holder.textViewWord.text = history.word

        holder.textViewSpeech.text = "[" + history.speech + "] "

        holder.textViewDefinition.text = history.definition

        holder.imageViewBookmark.setOnClickListener {
            HistoriesDao().removeFavorites(dbh, history.word)
            FavoritesDao().deleteFavorites(dbhf, history.word)
            holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark)
            notifyItemRemoved(position)
            historyList = FavoritesDao().getFavorites(dbhf)
            notifyItemRangeChanged(position, historyList.size)
            it.findNavController().popBackStack(R.id.favoritesFragment,true)
            it.findNavController().navigate(R.id.favoritesFragment)
        }

        holder.linearLayoutDesign.setOnClickListener { listener(history.word) }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}