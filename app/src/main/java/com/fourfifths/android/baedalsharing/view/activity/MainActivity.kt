package com.fourfifths.android.baedalsharing.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fourfifths.android.baedalsharing.*
import com.fourfifths.android.baedalsharing.databinding.ActivityMainBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.view.fragment.ChattingFragment
import com.fourfifths.android.baedalsharing.view.fragment.CommunityFragment
import com.fourfifths.android.baedalsharing.view.fragment.MatchingFragment
import com.fourfifths.android.baedalsharing.view.fragment.MyPageFragment
import com.fourfifths.android.baedalsharing.viewmodel.CommunityViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: CommunityViewModel by viewModels()
    private val TAG = MainActivity::class.java.simpleName

    /*private val matchingFragment = MatchingFragment()
    private val communityFragment = CommunityFragment()
    private val chattingFragment = ChattingFragment()
    private val myPageFragment = MyPageFragment()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        supportActionBar!!.title = "매칭"

        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuthUtils.getUid()!!
        db.collection("UserInfo").document(uid).get().addOnSuccessListener { document ->
            if(document.data != null) {
                App.prefs.setChatName(document.data!!["nickname"] as String)
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                finish()
            }

        setNavigation()

/*        supportFragmentManager.beginTransaction().replace(R.id.flMain, matchingFragment).commit()

        binding.mnuSelect.setOnItemSelectedListener { item ->
            supportActionBar!!.title = when(item.itemId){
                R.id.select_matching -> "매칭"
                R.id.select_community -> "커뮤니티"
                R.id.select_chatting -> "채팅"
                else -> "마이 페이지"
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.flMain, when (item.itemId) {
                    R.id.select_matching -> matchingFragment
                    R.id.select_community -> communityFragment
                    R.id.select_chatting -> chattingFragment
                    else -> myPageFragment
                }
            ).commit()
            true
        }*/
    }

    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateFragment(this, navHostFragment.childFragmentManager, binding.fcvMain.id)

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_graph)
        binding.mnuSelect.setupWithNavController(navController)
    }
}