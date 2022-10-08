package com.fourfifths.android.baedalsharing.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fourfifths.android.baedalsharing.databinding.FragmentMyPageBinding
import com.fourfifths.android.baedalsharing.view.activity.IntroActivity
import com.fourfifths.android.baedalsharing.view.activity.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            updateUI()
        }

        return binding.root
    }

    private fun updateUI() {
        val intent = Intent(context, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        (activity as MainActivity).finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
