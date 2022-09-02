package com.example.dictionaryapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.view.db_favorites.FavoritesDao
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.Histories
import com.example.dictionaryapp.view.db_history.HistoriesDao


class RVAdapterHistory (private val mContext: Context, private var historyList:List<Histories>, val listener: (String) -> Unit) : RecyclerView.Adapter<RVAdapterHistory.CardDesignObjectsHolder>() {

    private lateinit var dbh: DatabaseHelper

    inner class CardDesignObjectsHolder(view: View):RecyclerView.ViewHolder(view) {
        var textViewWord:TextView
        var imageViewDelete: ImageView
        var linearLayoutDesign: LinearLayout

        init {
            textViewWord = view.findViewById(R.id.tv_word)
            linearLayoutDesign = view.findViewById(R.id.ll_last_searches_design)
            imageViewDelete = view.findViewById(R.id.iv_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.last_searches_design, parent, false)
        return CardDesignObjectsHolder(design)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val history = historyList[position]

        dbh = DatabaseHelper(mContext)

        holder.textViewWord.text = history.word

        holder.imageViewDelete.setOnClickListener {
            HistoriesDao().deleteWord(dbh, history.word)
            notifyItemRemoved(position)
            historyList = HistoriesDao().getHistory(dbh)
            notifyItemRangeChanged(position, historyList.size)
        }

        holder.linearLayoutDesign.setOnClickListener { listener(history.word) }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}