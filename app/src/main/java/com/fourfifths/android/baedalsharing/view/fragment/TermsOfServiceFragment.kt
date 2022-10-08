package com.fourfifths.android.baedalsharing.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.FragmentTermsOfServiceBinding
import com.fourfifths.android.baedalsharing.view.activity.SignUpActivity
import com.fourfifths.android.baedalsharing.viewmodel.SignUpViewModel

class TermsOfServiceFragment : Fragment() {
    private var _binding: FragmentTermsOfServiceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_terms_of_service, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnAccept.setOnClickListener {
            (activity as SignUpActivity).changeFragment()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}