package com.fourfifths.android.baedalsharing.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.ActivitySignUpBinding
import com.fourfifths.android.baedalsharing.view.fragment.CompleteSignUpFragment
import com.fourfifths.android.baedalsharing.view.fragment.TermsOfServiceFragment
import com.fourfifths.android.baedalsharing.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var transaction: FragmentTransaction
    private val viewModel: SignUpViewModel by viewModels()

    private val termsOfServiceFragment = TermsOfServiceFragment()
    private val completeSignUpFragment = CompleteSignUpFragment()

    private val TAG = SignUpActivity::class.java.simpleName

    private var currentFragment = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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