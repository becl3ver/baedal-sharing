package com.fourfifths.android.baedalsharing.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.databinding.ActivityIntroBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.view.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private val db = FirebaseFirestore.getInstance()

    private val TAG = IntroActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        auth = Firebase.auth

        setGoogleLogin()
    }

    private fun updateUI() {
        val uid = FirebaseAuthUtils.getUid()!!
        db.collection("UserInfo").document(uid).get().addOnSuccessListener { document ->
            startActivity(
                Intent(
                    this,
                    if (document.data != null) MainActivity::class.java else SignUpActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            finish()
        }
            .addOnFailureListener { exception ->
                Toast.makeText(this@IntroActivity, "회원 등록에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "get failed with ", exception)
                finish()
            }
    }

    private fun setGoogleLogin() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

        binding.btnGoogleSignIn.setOnClickListener {
            startActivityForResult(client.signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Login Success")
                            updateUI()
                        }
                    })
            } catch (e: ApiException) {
                Log.d(TAG, "get failed with ${e.message}")
            }
        }
    }
}