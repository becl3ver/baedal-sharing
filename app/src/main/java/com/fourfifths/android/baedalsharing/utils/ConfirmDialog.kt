package com.fourfifths.android.baedalsharing.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fourfifths.android.baedalsharing.databinding.DialogConfirmBinding

class ConfirmDialog(
    private val confirmationDialogInterface: ConfirmationDialogInterface,
    private val message: String,
    private val request: Int
) : DialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)

        binding.tvMessage.text = message

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            confirmationDialogInterface.onConfirmButtonClick(request)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ConfirmationDialogInterface {
    fun onConfirmButtonClick(request: Int)
}