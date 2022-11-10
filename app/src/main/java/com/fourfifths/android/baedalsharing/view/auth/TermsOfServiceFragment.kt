package com.fourfifths.android.baedalsharing.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.FragmentTermsOfServiceBinding
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
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.btnAccept.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_termsOfServiceFragment_to_completeSignUpFragment)
        }

        viewModel.isAgreed.observe(viewLifecycleOwner, Observer {
            binding.btnAccept.background = ContextCompat.getDrawable(
                requireContext(),
                if (it) R.drawable.view_main_color else R.drawable.view_grey_color
            )
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}