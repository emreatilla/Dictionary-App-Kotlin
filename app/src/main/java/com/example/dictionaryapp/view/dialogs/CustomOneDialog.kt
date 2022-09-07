package com.example.dictionaryapp.view.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.dictionaryapp.R
import com.example.dictionaryapp.databinding.FragmentCustomOneDialogBinding
import com.example.dictionaryapp.view.db_history.DatabaseHelper
import com.example.dictionaryapp.view.db_history.HistoriesDao
import com.example.dictionaryapp.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomOneDialog : DialogFragment() {
    private var isDeleteOK: Boolean = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = FragmentCustomOneDialogBinding.inflate(layoutInflater)
        val dbh = DatabaseHelper(requireContext())
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
            isDeleteOK = true
            model.sendBoolean(isDeleteOK)
        }
        binding.btnDone.setOnClickListener {
            HistoriesDao().deleteAllRecords(dbh)
            dialog?.dismiss()
            isDeleteOK = true
            model.sendBoolean(isDeleteOK)
        }

        val dialog = MaterialAlertDialogBuilder(requireActivity(), R.style.BottomSheetDialog
        ).apply {
            setView(binding.root)

            setOnKeyListener { _, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    dismiss()
                    true
                } else false
            }

        }.create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

}