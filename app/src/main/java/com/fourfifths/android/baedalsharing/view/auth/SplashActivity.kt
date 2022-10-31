package com.fourfifths.android.baedalsharing.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.databinding.ActivitySplashBinding
import com.fourfifths.android.baedalsharing.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    private val TAG = SplashActivity::class.simpleName

    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            checkCompletionOfSignUp()
        }, 2000)
    }

    private fun checkCompletionOfSignUp() {
        val uid = FirebaseAuthUtils.getUid()
        Log.d(TAG, uid.toString())
        if (uid == null) {
            startActivity(
                Intent(
                    this, IntroActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            finish()
        } else {
            db.collection("UserInfo").document(uid).get().addOnSuccessListener { document ->
                Log.d(TAG, document.toString())
                startActivity(
                    Intent(
                        this,
                        if (document.data != null) MainActivity::class.java else SignUpActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
                finish()
            }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ${exception.message}")
                    finish()
                }
        }
    }
}