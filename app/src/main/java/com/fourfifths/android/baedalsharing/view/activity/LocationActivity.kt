package com.fourfifths.android.baedalsharing.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fourfifths.android.baedalsharing.LocationProvider
import com.fourfifths.android.baedalsharing.databinding.ActivityLocationBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLocationBinding
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private var currentLocation: LatLng? = null

    private val getGPSPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (isLocationServicesEnabled()) {
                    checkPermissionGranted()
                } else {
                    Toast.makeText(
                        this@LocationActivity,
                        "위치 서비스를 사용할 수 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }

    private val PERMISSION_REQUEST_CODE = 1000
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.btnSubmit.background.alpha = 200

        setCurrentLocation()

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.btnSubmit.setOnClickListener {
            if (currentLocation == null) {
                setResult(RESULT_CANCELED)
                finish()
            } else {
                val intent = Intent()
                intent.putExtra("latitude", currentLocation!!.latitude)
                intent.putExtra("longitude", currentLocation!!.longitude)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun setCurrentLocation() {
        if (!isLocationServicesEnabled()) {
            checkGPSServiceTurnedOn()
        } else {
            checkPermissionGranted()
        }

        val locationProvider = LocationProvider(this)
        currentLocation = locationProvider.getLatLng()
    }

    private fun isLocationServicesEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkGPSServiceTurnedOn() {
        AlertDialog.Builder(this@LocationActivity)
            .setTitle("위치 서비스 설정")
            .setMessage("위치 서비스가 꺼져 있습니다. 위치 서비스가 켜져 있어야 매칭이 가능합니다.")
            .setPositiveButton("설정") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                getGPSPermissionLauncher.launch(intent)
            }
            .setNegativeButton("취소") { _, _ ->
                Toast.makeText(this@LocationActivity, "위치 서비스 허용 후 사용해주세요.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            .create()
            .show()
    }

    private fun checkPermissionGranted() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@LocationActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@LocationActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED
            || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@LocationActivity,
                REQUIRED_PERMISSIONS,
                PERMISSION_REQUEST_CODE
            )
        }
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true

        naverMap.minZoom = 10.0
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow

        if (currentLocation != null) {
            val cameraUpdate = CameraUpdate.scrollTo(currentLocation!!)
            naverMap.moveCamera(cameraUpdate)
        }

        val marker = Marker()
        marker.position = naverMap.cameraPosition.target
        marker.map = naverMap
        marker.icon =
            OverlayImage.fromResource(com.fourfifths.android.baedalsharing.R.drawable.ic_baseline_location_on_24)

        naverMap.addOnCameraChangeListener { _, _ ->
            marker.position = naverMap.cameraPosition.target
            currentLocation = naverMap.cameraPosition.target
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                Toast.makeText(this@LocationActivity, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}