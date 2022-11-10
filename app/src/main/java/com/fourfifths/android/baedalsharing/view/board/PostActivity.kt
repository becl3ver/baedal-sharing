package com.fourfifths.android.baedalsharing.view.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourfifths.android.baedalsharing.view.App
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Post
import com.fourfifths.android.baedalsharing.data.firebase.model.community.CommentDataModel
import com.fourfifths.android.baedalsharing.databinding.ActivityPostBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.viewmodel.CommentViewModel
import com.fourfifths.android.baedalsharing.viewmodel.PostViewModelFactory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class PostActivity : AppCompatActivity() {
    private val TAG = PostActivity::class.java.simpleName

    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: CommentViewModel
    private lateinit var viewModelFactory: PostViewModelFactory
    private lateinit var adapter: PostRecyclerViewAdapter
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        supportActionBar?.hide()

        post = intent.getSerializableExtra("board") as Post

        viewModelFactory = PostViewModelFactory(post.id)
        viewModel = ViewModelProvider(this, viewModelFactory)[CommentViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerView()
        initObserver()

        viewModel.getComments()

        binding.btnSubmit.setOnClickListener {
            if (binding.etNewComment.text.isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val db = FirebaseFirestore.getInstance()

                val comment = CommentDataModel(
                    FirebaseAuthUtils.getUid()!!,
                    App.prefs.getNickname()!!,
                    Timestamp.now(),
                    binding.etNewComment.text.toString()
                )

                db.collection("Boards")
                    .document(post.id).collection("Comments")
                    .add(comment)
                    .addOnSuccessListener {
                        Toast.makeText(this, "작성을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                        binding.etNewComment.setText("")
                        viewModel.getComments()
                    }.addOnFailureListener {
                        Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.message.toString())
                    }

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etNewComment.windowToken, 0)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = PostRecyclerViewAdapter(post)
        binding.rvBoard.adapter = adapter
        binding.rvBoard.layoutManager = LinearLayoutManager(this)
    }

    private fun initObserver() {
        viewModel.comments.observe(this, Observer {
            adapter.addComments(it)
            adapter.notifyItemRangeChanged(0, it.size + 1)
        })
    }
}