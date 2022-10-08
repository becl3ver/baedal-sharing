package com.fourfifths.android.baedalsharing.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.data.remote.model.board.BoardDataModel
import com.fourfifths.android.baedalsharing.databinding.ActivityNewBoardBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.viewmodel.NewBoardViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NewBoardActivity : AppCompatActivity() {
    private val TAG = NewBoardViewModel::class.java.simpleName

    private lateinit var binding: ActivityNewBoardBinding
    private lateinit var viewModel: NewBoardViewModel
    private var category = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_board)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewBoardViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        category = intent.getIntExtra("category", 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_board_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

            }
            R.id.actionSubmit -> {
                pushNewBoard()
            }
        }
        return true
    }

    private fun pushNewBoard() {
        val title = viewModel.boardTitle.value!!
        val content = viewModel.boardContent.value!!

        if (title.isEmpty() || content.isEmpty()) {
            //(ง •̀_•́)ง

            AlertDialog.Builder(this).let {
                // TODO : 다이얼로그 레이아웃 하나 먼들고 클릭리스너 처리
                it.create()
                it.show()
            }

            return
        }

        val author = FirebaseAuthUtils.getUid()!!

        val boardDataModel = BoardDataModel(
            title,
            content,
            category,
            author
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Boards").add(boardDataModel)
            .addOnSuccessListener {
                Toast.makeText(this@NewBoardActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "failed with $exception")
                // TODO : 오류 안내
            }
    }
}