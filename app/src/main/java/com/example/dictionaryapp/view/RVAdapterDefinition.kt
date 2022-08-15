package com.example.dictionaryapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.model.Definition

class RVAdapterDefinition(private val mContext: Context, private val definitionList:List<Definition>, private val speech: String) : RecyclerView.Adapter<RVAdapterDefinition.CardDesignObjectsHolder>() {

    inner class CardDesignObjectsHolder(view: View):RecyclerView.ViewHolder(view){
        var textViewDefinition:TextView
        var textViewExample:TextView
        var textViewDefCount: TextView

        init {
            textViewDefinition = view.findViewById(R.id.tvDefinition)
            textViewExample = view.findViewById(R.id.tvExample)
            textViewDefCount = view.findViewById(R.id.tvDefCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapterDefinition.CardDesignObjectsHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.definition_card, parent, false)
        return CardDesignObjectsHolder(design)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RVAdapterDefinition.CardDesignObjectsHolder, position: Int) {
        val definition = definitionList[position]

        if (definition.example != null) {
            holder.textViewExample.text = definition.example
            Log.e("???", definition.example)
        } else {
            holder.textViewExample.visibility = View.GONE
        }
        holder.textViewDefinition.text = definition.definition
        holder.textViewDefCount.text = (position + 1).toString() + speech
    }

    override fun getItemCount(): Int {
        return definitionList.size
    }
}