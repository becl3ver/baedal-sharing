package com.fourfifths.android.baedalsharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fourfifths.android.baedalsharing.databinding.DialogCategoryBinding

class CategoryDialog(
    private val categoryDialogInterface: CategoryDialogInterface
) : DialogFragment(), View.OnClickListener {
    private var _binding: DialogCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCategoryBinding.inflate(inflater, container, false)

        binding.btnLookFor.setOnClickListener(this)
        binding.btnQnA.setOnClickListener(this)
        binding.btnFree.setOnClickListener(this)
        binding.btnRecommend.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View) {
        categoryDialogInterface.onCategoryButtonClick(
            when (v.id) {
                binding.btnLookFor.id -> 0
                binding.btnQnA.id -> 1
                binding.btnFree.id -> 2
                else -> 3
            }
        )

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface CategoryDialogInterface {
    fun onCategoryButtonClick(category: Long)
}