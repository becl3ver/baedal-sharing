package com.fourfifths.android.baedalsharing.view.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.*
import com.fourfifths.android.baedalsharing.databinding.ActivityNewPostBinding
import com.fourfifths.android.baedalsharing.utils.NoticeDialog
import com.fourfifths.android.baedalsharing.viewmodel.NewPostActivity

class NewPostActivity : AppCompatActivity() {
    private val TAG = NewPostActivity::class.simpleName

    private lateinit var binding: ActivityNewPostBinding
    private lateinit var viewModel: NewPostActivity
    private var category: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)
        setContentView(binding.root)

        category = intent.getLongExtra("category", 0)

        viewModel = ViewModelProvider(this)[NewPostActivity::class.java]
        viewModel.category = category
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "새 글 작성"

        initObserver()
    }

    private fun initObserver() {
        viewModel.isEmptyEtExist.observe(this, Observer {
            if (it) {
                val dialog = NoticeDialog("제목과 내용은 모두 작성해야 합니다.")
                dialog.show(supportFragmentManager, "CategoryDialog")
                viewModel.initFlags()
            }
        })

        viewModel.submitResponse.observe(this, Observer {
            when (it) {
                1 -> {
                    Toast.makeText(this@NewPostActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                2 -> {
                    Toast.makeText(this@NewPostActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_board_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSubmit -> {
                viewModel.pushNewPost()
            }
        }

        return true
    }
}