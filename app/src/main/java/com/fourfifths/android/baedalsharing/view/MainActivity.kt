package com.fourfifths.android.baedalsharing.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fourfifths.android.baedalsharing.*
import com.fourfifths.android.baedalsharing.databinding.ActivityMainBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.viewmodel.CommunityViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: CommunityViewModel
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[CommunityViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.hide()

        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuthUtils.getUid()!!
        db.collection("UserInfo").document(uid).get().addOnSuccessListener { document ->
            if(document.data != null) {
                App.prefs.setNickname(document.data!!["nickname"] as String)
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                finish()
            }

        setNavigation()
    }

    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateFragment(this, navHostFragment.childFragmentManager, binding.fcvMain.id)

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.main_nav_graph)
        binding.mnuSelect.setupWithNavController(navController)
    }
}