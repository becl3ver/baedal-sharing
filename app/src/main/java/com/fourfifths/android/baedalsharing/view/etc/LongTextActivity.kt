package com.fourfifths.android.baedalsharing.view.etc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourfifths.android.baedalsharing.databinding.ActivityLongTextBinding
import com.google.gson.Gson
import java.io.IOException

class LongTextActivity : AppCompatActivity() {
    private val TAG = LongTextActivity::class.java.simpleName

    private lateinit var binding: ActivityLongTextBinding
    private lateinit var adapter: LongTextRecyclerViewAdapter
    private lateinit var items: ArrayList<LongText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLongTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filename = intent.getStringExtra("filename") ?: ""
        /*getJsonData(filename)*/
        /*setRecyclerView()*/
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = LongTextRecyclerViewAdapter(items)
            binding.rvLontTextList.adapter = adapter
            binding.rvLontTextList.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun getJsonData(filename: String) {
        var jsonString = ""

        try {
            jsonString = assets.open(filename).reader().readText()
        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
            Toast.makeText(this, "파일을 읽어 오는데 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val result = Gson().fromJson(jsonString, Document::class.java)
        items = result.items
    }
}