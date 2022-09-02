package com.example.dictionaryapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.view.db_favorites.DatabaseHelperFavorites
import com.example.dictionaryapp.view.db_favorites.Favorites
import com.example.dictionaryapp.view.db_favorites.FavoritesDao
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao

class RVAdapterFavorites (private val mContext: Context, private var favList:List<Favorites>, val listener: (String) -> Unit) : RecyclerView.Adapter<RVAdapterFavorites.CardDesignObjectsHolder>() {

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
            linearLayoutDesign = view.findViewById(R.id.ll_last_searches_design)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.favorites_design, parent, false)
        return CardDesignObjectsHolder(design)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val history = favList[position]

        dbh = DatabaseHelper(mContext)
        dbhf = DatabaseHelperFavorites(mContext)

        holder.textViewWord.text = history.word

        holder.textViewSpeech.text = "[" + history.speech + "] "

        if (HistoriesDao().isFavorite(dbh, history.word) == 1) {
            holder.imageViewBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
        }

        if (history.definition.length > 55) {
            holder.textViewDefinition.text = history.definition.take(53) + "..."
        }
        else {
            holder.textViewDefinition.text = history.definition
        }

        holder.imageViewBookmark.setOnClickListener {
            HistoriesDao().removeFavorites(dbh, history.word)
            FavoritesDao().deleteFavorites(dbhf, history.word)
            holder.imageViewBookmark.setImageResource(R.drawable.ic_bookmark)
            notifyItemRemoved(position)
            favList = FavoritesDao().getFavorites(dbhf)
            notifyItemRangeChanged(position, favList.size)
            Log.e("list", favList.toString())
            // HistoriesDao().addToFavorites(dbh, history.word)
            // Toast.makeText(mContext, "${history.word} bookmark clicked", Toast.LENGTH_SHORT).show()
            // This method refreshes the fragment
            it.findNavController().popBackStack(R.id.homePageFragment,true)
            it.findNavController().navigate(R.id.homePageFragment)
        }

        holder.linearLayoutDesign.setOnClickListener { listener(history.word) }

    }

    override fun getItemCount(): Int {
        return favList.size
    }
}