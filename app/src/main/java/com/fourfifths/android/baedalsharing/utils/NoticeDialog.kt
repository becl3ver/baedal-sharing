package com.fourfifths.android.baedalsharing.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fourfifths.android.baedalsharing.databinding.DialogNoticeBinding

class NoticeDialog(private val message: String) : DialogFragment() {
    private var _binding: DialogNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNoticeBinding.inflate(inflater, container, false)

        binding.tvMessage.text = message

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}