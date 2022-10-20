package com.fourfifths.android.baedalsharing.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.FragmentMatchingBinding
import com.fourfifths.android.baedalsharing.view.activity.MatchingActivity
import kotlin.random.Random

class MatchingFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMatchingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matching, container, false)

        initButton()

        return binding.root
    }

    private fun initButton() {
        binding.btnChicken.setOnClickListener(this)
        binding.btnPizza.setOnClickListener(this)
        binding.btnBurger.setOnClickListener(this)
        binding.btnJapanese.setOnClickListener(this)
        binding.btnChicken.setOnClickListener(this)
        binding.btnKorean.setOnClickListener(this)
        binding.btnSnack.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        val menu = when (v?.id) {
            binding.btnChicken.id -> 1
            binding.btnPizza.id -> 2
            binding.btnBurger.id -> 3
            binding.btnJapanese.id -> 4
            binding.btnChicken.id -> 5
            binding.btnKorean.id -> 6
            binding.btnSnack.id -> 7
            else -> Random.nextInt(7) + 1
        }

        val intent = Intent(context, MatchingActivity::class.java)
        intent.putExtra("menu", menu)
        startActivity(intent)
    }
}