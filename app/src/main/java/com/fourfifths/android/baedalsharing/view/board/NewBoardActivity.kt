package com.fourfifths.android.baedalsharing.view.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.*
import com.fourfifths.android.baedalsharing.data.remote.model.board.BoardDataModel
import com.fourfifths.android.baedalsharing.databinding.ActivityNewBoardBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.utils.NoticeDialog
import com.fourfifths.android.baedalsharing.view.App
import com.fourfifths.android.baedalsharing.viewmodel.NewBoardViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class NewBoardActivity : AppCompatActivity() {
    private val TAG = NewBoardViewModel::class.java.simpleName

    private lateinit var binding: ActivityNewBoardBinding
    private lateinit var viewModel: NewBoardViewModel
    private var category: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_board)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewBoardViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "새 글 작성"

        category = intent.getLongExtra("category", -1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_board_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSubmit -> {
                postNewBoard()
            }
        }
        return true
    }

    private fun postNewBoard() {
        val title = viewModel.boardTitle.value!!
        val content = viewModel.boardContent.value!!

        if (title.isEmpty() || content.isEmpty()) {
            val dialog = NoticeDialog("제목과 내용은 모두 작성해야 합니다.")
            dialog.show(supportFragmentManager, "CategoryDialog")

            return
        }

        val boardDataModel = BoardDataModel(
            title,
            content,
            category,
            FirebaseAuthUtils.getUid()!!,
            App.prefs.getNickname()!!,
            0.toLong(),
            Timestamp.now()
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("Boards").add(boardDataModel)
            .addOnSuccessListener {
                Toast.makeText(this@NewBoardActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@NewBoardActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT)
                    .show()
                Log.d(TAG, "failed with $exception")
            }
    }
}