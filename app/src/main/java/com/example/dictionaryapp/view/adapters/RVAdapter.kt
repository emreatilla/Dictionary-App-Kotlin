package com.example.dictionaryapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.model.DictionaryModelItem

class RVAdapter(private val mContext: Context, private val itemList:List<DictionaryModelItem>) : RecyclerView.Adapter<RVAdapter.CardDesignObjectsHolder>() {

    inner class CardDesignObjectsHolder(view:View):RecyclerView.ViewHolder(view){
        var textViewWord:TextView
        var textViewCount:TextView
        var textViewPhonetic:TextView
        var childRV: RecyclerView

        init {
            textViewWord = view.findViewById(R.id.twWord)
            textViewCount = view.findViewById(R.id.twCount)
            textViewPhonetic = view.findViewById(R.id.twPhonetic)
            childRV = view.findViewById(R.id.rvMeaning)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.word_card, parent, false)
        return CardDesignObjectsHolder(design)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val def = itemList[position]

        if (itemList.size > 1) {
            holder.textViewCount.text = "(${position + 1})"
        }
        holder.textViewWord.text = def.word
        holder.textViewPhonetic.text = def.phonetic
        holder.childRV.setHasFixedSize(true)
        holder.childRV.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val adapterMeaning = RVAdapterMeaning(mContext, def.meanings)
        holder.childRV.adapter = adapterMeaning

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}