package com.example.dictionaryapp.other

import android.annotation.SuppressLint
import com.example.dictionaryapp.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


object MyToast {
    @SuppressLint("InflateParams")
    fun show(context: Context?, text: String?, isLong: Boolean, imageCheck: Int) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast_message, null)
        val image: ImageView = layout.findViewById(R.id.iv_toast) as ImageView
        if (imageCheck == 1){
            image.setImageResource(R.drawable.ic_baseline_info_24)
        } else {
            image.setImageResource(R.drawable.ic_copy)
        }

        val textV = layout.findViewById(R.id.tv_message) as TextView
        textV.text = text
        val toast = Toast(context)
        toast.duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        toast.setView(layout)
        toast.show()
    }
}