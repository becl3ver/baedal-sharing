package com.fourfifths.android.baedalsharing.view.matching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fourfifths.android.baedalsharing.R
import com.fourfifths.android.baedalsharing.data.remote.model.matching.MatchingRequestDto
import com.fourfifths.android.baedalsharing.databinding.ActivityMatchingBinding
import com.fourfifths.android.baedalsharing.utils.FirebaseAuthUtils
import com.fourfifths.android.baedalsharing.viewmodel.MatchingViewModel

class MatchingActivity : AppCompatActivity() {
    private val TAG = MatchingActivity::class.java.simpleName

    private lateinit var binding: ActivityMatchingBinding
    private lateinit var getLocationLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: MatchingViewModel

    private var menu: Int = 0
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_matching)

        viewModel = ViewModelProvider(this)[MatchingViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "매칭"

        menu = intent.getIntExtra("menu", 0)

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

        initObserver()
        initNumberPickers()

        binding.btnLocation.setOnClickListener {
            getLocationLauncher.launch(Intent(this, LocationActivity::class.java))
        }

        binding.btnSubmit.setOnClickListener {
            val isCurrent = binding.rgTime.checkedRadioButtonId == R.id.rbNow
            val uid = FirebaseAuthUtils.getUid()!!
            val matchingRequestDto = MatchingRequestDto(
                uid,
                binding.rgTime.checkedRadioButtonId == R.id.rbNow,
                latitude!!,
                longitude!!,
                if(isCurrent) 0 else getDueDay(),
                if(isCurrent) 0 else getDueTime()
            )

            viewModel.setMatching(uid, matchingRequestDto)
            finish()
        }
    }

    private fun initObserver() {
        viewModel.matchingJoinResult.observe(this, Observer {
            if(it.equals("success")) {
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            Log.d(TAG, it)
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        })

        viewModel.isNowChecked.observe(this, Observer {
            if(it) {
                binding.llTimePickerContainer.visibility = View.VISIBLE
            } else {
                binding.llTimePickerContainer.visibility = View.GONE
            }
        })
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