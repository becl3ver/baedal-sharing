package com.fourfifths.android.baedalsharing.view.matching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import com.fourfifths.android.baedalsharing.data.remote.repository.MatchingRepository
import com.fourfifths.android.baedalsharing.databinding.ActivityMatchingBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils

class MatchingActivity : AppCompatActivity() {
    private val TAG = MatchingActivity::class.java.simpleName

    private lateinit var binding: ActivityMatchingBinding
    private lateinit var getLocationLauncher: ActivityResultLauncher<Intent>

    private var menu: Int = 0
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "매칭"

        initNumberPickers()

        menu = intent.getIntExtra("menu", 0)

        if (menu < 1 || menu > 7) {
            Toast.makeText(this, "올바른 메뉴가 아닙니다.", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "menu isn't selected : $menu")
            finish()
        }

        getLocationLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                latitude = result.data?.extras?.get("latitude") as Double
                longitude = result.data?.extras?.get("longitude") as Double

                binding.tvResult.text = "위치 설정이 완료되었습니다."

                binding.btnSubmit.background = AppCompatResources.getDrawable(this, R.drawable.view_main_color)
                binding.btnSubmit.isEnabled = true

                Log.d(TAG, "위도 : ${latitude.toString()}, 경도 : ${longitude.toString()}")
            }
        }

        binding.btnLocation.setOnClickListener {
            getLocationLauncher.launch(Intent(this, LocationActivity::class.java))
        }

        binding.btnSubmit.setOnClickListener {
            val isCurrent = binding.rbNow.isChecked
            var day = 0
            var time = 0

            if (!isCurrent) {
                day = getDueDay()
                time = getDueTime()
            }

            val repository = MatchingRepository()
            val token = FirebaseAuthUtils.getUid()!!
            val matchingRequestDto = MatchingRequestDto(
                FirebaseAuthUtils.getUid()!!,
                isCurrent,
                latitude!!,
                longitude!!,
                day,
                time
            )

            val result = repository.setMatching(token, matchingRequestDto)

            Toast.makeText(
                this,
                if (result == "success") "매칭이 시작되었습니다." else "오류가 발생했습니다.",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun initNumberPickers() {
        val days = resources.getStringArray(R.array.days)
        binding.npDay.minValue = 1
        binding.npDay.maxValue = days.size
        binding.npDay.displayedValues = days
        binding.npDay.value = 1

        val minute = resources.getStringArray(R.array.minute)
        binding.npMinute.minValue = 1
        binding.npMinute.maxValue = minute.size
        binding.npMinute.displayedValues = minute
        binding.npDay.value = 1
    }

    private fun getDueDay(): Int {
        return binding.npDay.value
    }

    private fun getDueTime(): Int {
        val hour = binding.npHour.value
        val minute = binding.npMinute.value
        return hour * 60 + minute
    }
}