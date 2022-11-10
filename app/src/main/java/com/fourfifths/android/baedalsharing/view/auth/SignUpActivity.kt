package com.fourfifths.android.baedalsharing.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.ActivitySignUpBinding
import com.fourfifths.android.baedalsharing.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    private val TAG = SignUpActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        supportActionBar?.hide()
    }
}