package com.example.dictionaryapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.view.db_history.Histories

class RVAdapterHistoryFragment (private val mContext: Context, private val historyList:List<Histories>, val listener: (String) -> Unit) : RecyclerView.Adapter<RVAdapterHistoryFragment.CardDesignObjectsHolder>() {

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

        holder.textViewWord.text = history.word

        holder.textViewSpeech.text = "[" + history.speech + "] "

        holder.textViewDefinition.text = history.definition

        /*
        if (history.definition.length > 55) {
            holder.textViewDefinition.text = history.definition.take(53) + "..."
        }
        else {
            holder.textViewDefinition.text = history.definition
        }
         */

        holder.imageViewBookmark.setOnClickListener {
            Toast.makeText(mContext, "${history.word} bookmark clicked", Toast.LENGTH_SHORT).show()
        }

        holder.linearLayoutDesign.setOnClickListener { listener(history.word) }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}