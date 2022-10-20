package com.fourfifths.android.baedalsharing.view.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.fourfifths.android.baedalsharing.App
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

        initNumberPickers()

        menu = intent.getIntExtra("menu", 0)

        if (menu == 0) {
            finish()
        }

        getLocationLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                latitude = result.data?.extras?.get("latitude") as Double
                longitude = result.data?.extras?.get("longitude") as Double

                binding.tvResult.text = "위치 설정이 완료되었습니다."

                Toast.makeText(
                    this@MatchingActivity,
                    "위도 : ${latitude.toString()}, 경도 : ${longitude.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnLocation.setOnClickListener {
            getLocationLauncher.launch(Intent(this@MatchingActivity, LocationActivity::class.java))
        }

        binding.btnSubmit.setOnClickListener {
            val repository = MatchingRepository()
            val token = FirebaseAuthUtils.getUid()!!
            val matchingRequestDto = MatchingRequestDto(
                FirebaseAuthUtils.getUid()!!,
                true,
                latitude!!,
                longitude!!,
                0,
                0
            )

            Toast.makeText(
                this@MatchingActivity,
                repository.setMatchingNow(token, matchingRequestDto), Toast.LENGTH_SHORT
            ).show()
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
}