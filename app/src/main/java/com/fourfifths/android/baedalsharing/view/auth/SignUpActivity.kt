package com.fourfifths.android.baedalsharing.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.ActivitySignUpBinding
import com.fourfifths.android.baedalsharing.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    private val termsOfServiceFragment = TermsOfServiceFragment()
    private val completeSignUpFragment = CompleteSignUpFragment()

    private val TAG = SignUpActivity::class.simpleName

    private var currentFragment = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        supportActionBar?.hide()

        supportFragmentManager.beginTransaction().replace(R.id.flSignUp, termsOfServiceFragment).commit()
    }

    fun changeFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.flSignUp, when(currentFragment) {
                1 -> {
                    currentFragment = 2
                    completeSignUpFragment
                }
                else -> {
                    currentFragment = 1
                    termsOfServiceFragment
                }
            }
        ).commit()
    }

    override fun onBackPressed() {
        if(currentFragment == 2) {
            changeFragment()
        } else {
            finish()
        }
    }
}