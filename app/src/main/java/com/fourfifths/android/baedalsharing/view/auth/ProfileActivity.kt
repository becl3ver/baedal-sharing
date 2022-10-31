package com.fourfifths.android.baedalsharing.view.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.canhub.cropper.*
import com.fourfifths.android.baedalsharing.view.App
import com.fourfifths.android.baedalsharing.databinding.ActivityProfileBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProfileActivity : AppCompatActivity() {
    private val TAG = ProfileActivity::class.simpleName

    private val getImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data.let { uri ->
                    startCrop(uri)
                }
            } else {
                Log.d(TAG, "failed to get image")
            }
        }


    private val cropImage =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val uriContent = result.uriContent
                binding.ivProfile.setImageURI(uriContent)
                uri = uriContent
            } else {
                val exception = result.error
                exception?.printStackTrace()
            }
        }


    private lateinit var binding: ActivityProfileBinding

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        getProfileImage()

        binding.etNickname.setText(App.prefs.getNickname()!!)

        binding.btnChangeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
            intent.putExtra("crop", true)
            intent.action = Intent.ACTION_GET_CONTENT

            getImage.launch(intent)
        }

        binding.btnSubmit.setOnClickListener {
            if (!isNicknameValid(binding.etNickname.text.toString())) {
                Toast.makeText(this, "닉네임은 2 ~ 12글자의 한글 또는 영어만 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                updateNickname()
                uploadImage()
                Toast.makeText(this, "변경 사항이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getProfileImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val pathRef = storageRef.child("userProfiles").child(FirebaseAuthUtils.getUid()!!)
        pathRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful) {
                Glide.with(this)
                    .load(it.result)
                    .into(binding.ivProfile)
            }
        }
    }

    private fun startCrop(uri: Uri?) {
        cropImage.launch(
            CropImageContractOptions(
                uri,
                CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON,
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true
                )
            )
        )
    }

    private fun isNicknameValid(nickname: String): Boolean {
        return if (nickname.length < 2 || nickname.length > 10) {
            false
        } else {
            val regex = Regex("^[ㄱ-ㅣ가-힣a-zA-Z]+\$")
            regex.matches(nickname)
        }
    }

    private fun updateNickname() {
        val nickname = binding.etNickname.text.toString()

        if (nickname != App.prefs.getNickname()) {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("UserInfo").document(FirebaseAuthUtils.getUid()!!)
                .update("nickname", nickname)

            query.addOnFailureListener {
                Log.d(TAG, it.message.toString())
                App.prefs.setNickname(nickname)
            }
        }
    }

    private fun uploadImage() {
        if(uri == null) {
            return
        }

        val storage = Firebase.storage
        val storageRef = storage.reference.child("userProfiles").child(FirebaseAuthUtils.getUid()!!)

        val uploadTask = storageRef.putFile(uri!!)
        uploadTask.addOnFailureListener {
            Log.d(TAG, it.message.toString())
        }
    }
}