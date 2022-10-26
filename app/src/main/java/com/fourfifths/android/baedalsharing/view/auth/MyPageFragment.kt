package com.fourfifths.android.baedalsharing.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fourfifths.android.baedalsharing.view.App
import com.fourfifths.android.baedalsharing.databinding.FragmentMyPageBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.view.etc.LongTextActivity
import com.fourfifths.android.baedalsharing.view.MainActivity
import com.fourfifths.android.baedalsharing.view.matching.HistoryActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MyPageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        binding.tvNickname.text = (App.prefs.getNickname() + " 님")

        getProfileImage()
        initButton()

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.btnEditProfile.id -> {
                val intent = Intent(context, ProfileActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogout.id -> {
                Firebase.auth.signOut()
                updateUI()
            }
            binding.btnHistory.id -> {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
            }
            binding.btnLicense.id -> {
                val intent = Intent(context, LongTextActivity::class.java).putExtra("filename", "license")
                startActivity(intent)
            }
            binding.btnTermsOfService.id -> {
                val intent = Intent(context, LongTextActivity::class.java).putExtra("filename", "tos")
                startActivity(intent)
            }
        }
    }

    private fun getProfileImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val pathRef = storageRef.child("userProfiles").child(FirebaseAuthUtils.getUid()!!)
        pathRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful) {
                Glide.with(requireContext())
                    .load(it.result)
                    .into(binding.ivProfile)
            }
        }
    }

    private fun initButton() {
        binding.btnEditProfile.setOnClickListener(this)
        binding.btnHistory.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnLicense.setOnClickListener(this)
        binding.btnTermsOfService.setOnClickListener(this)
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
