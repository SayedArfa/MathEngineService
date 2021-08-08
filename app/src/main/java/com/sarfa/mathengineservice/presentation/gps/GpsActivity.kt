package com.sarfa.mathengineservice.presentation.gps


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.sarfa.mathengineservice.databinding.ActivityGpsBinding

class GpsActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewBinding: ActivityGpsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGpsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.containsValue(false)) {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                getLastLocation()
            }

        }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationRequest =
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                viewBinding.locationTextView.text = "Location: " + location?.let {
                    "${it.longitude} : ${it.latitude}"
                }
            }
        }
    }

}