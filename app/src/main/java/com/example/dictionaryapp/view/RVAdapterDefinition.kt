package com.example.dictionaryapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.model.Definition

class RVAdapterDefinition(private val mContext: Context, private val definitionList:List<Definition>) : RecyclerView.Adapter<RVAdapterDefinition.CardDesignObjectsHolder>() {

    inner class CardDesignObjectsHolder(view: View):RecyclerView.ViewHolder(view){
        var textViewDefinition:TextView

        init {
            textViewDefinition = view.findViewById(R.id.tvDefinition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapterDefinition.CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.definition_card, parent, false)
        return CardDesignObjectsHolder(design)
    }

    override fun onBindViewHolder(holder: RVAdapterDefinition.CardDesignObjectsHolder, position: Int) {
        val definition = definitionList[position]

        holder.textViewDefinition.text = definition.definition
    }

    override fun getItemCount(): Int {
        return definitionList.size
    }
}