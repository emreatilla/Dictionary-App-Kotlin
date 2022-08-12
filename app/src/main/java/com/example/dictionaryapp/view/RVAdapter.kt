package com.example.dictionaryapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.model.Definition

class RVAdapter(private val mContext: Context, private val defList:List<Definition>) : RecyclerView.Adapter<RVAdapter.CardDesignObjectsHolder>() {

    inner class CardDesignObjectsHolder(view:View):RecyclerView.ViewHolder(view){
        var textView:TextView

        init {
            textView = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.word_card, parent, false)
        return CardDesignObjectsHolder(design)
    }

    override fun onBindViewHolder(holder: CardDesignObjectsHolder, position: Int) {
        val def = defList[position]

        holder.textView.text = def.definition
    }

    override fun getItemCount(): Int {
        return defList.size
    }
}