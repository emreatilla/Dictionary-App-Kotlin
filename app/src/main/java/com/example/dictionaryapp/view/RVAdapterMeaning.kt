package com.example.dictionaryapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dictionaryapp.R
import com.example.dictionaryapp.model.Meaning

class RVAdapterMeaning(private val mContext: Context, private val meaningList:List<Meaning>) : RecyclerView.Adapter<RVAdapterMeaning.CardDesignObjectsHolder>() {

    inner class CardDesignObjectsHolder(view:View):RecyclerView.ViewHolder(view){
        var textViewSpeech:TextView
        var childRV: RecyclerView

        init {
            textViewSpeech = view.findViewById(R.id.tvSpeech)
            childRV = view.findViewById(R.id.rvDefinition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.meaning_card, parent, false)
        return CardDesignObjectsHolder(design)
    }

    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val meaning = meaningList[position]

        holder.textViewSpeech.text = meaning.partOfSpeech
        holder.childRV.setHasFixedSize(true)
        holder.childRV.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        val adapterDefinition = RVAdapterDefinition(mContext, meaning.definitions, meaning.partOfSpeech)
        holder.childRV.adapter = adapterDefinition
    }

    override fun getItemCount(): Int {
        return meaningList.size
    }
}