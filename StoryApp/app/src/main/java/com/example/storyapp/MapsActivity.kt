package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.storyapp.data.Results

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.model.Story
import com.example.storyapp.util.LoadingDialog
import com.example.storyapp.util.SHARED_PREF_NAME
import com.example.storyapp.util.TOKEN
import com.example.storyapp.util.showMessage

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var loading: LoadingDialog
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: MapsViewModel by viewModels {
        factory
    }
    private val locationList = arrayListOf<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingDialog(this)

        getData()
    }

    private fun getData() {
        val token = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getString(TOKEN, "")
        val auth = "Bearer $token"

        viewModel.getStory(auth).observe(this) {
            when(it) {
                is Results.Loading -> {
                    loading.show()
                }
                is Results.Success -> {
                    loading.dismiss()
                    if(it.data.listStory.isNotEmpty()) {
                        for(story in it.data.listStory) {
                            locationList.add(story)
                        }

                        val mapFragment = supportFragmentManager
                            .findFragmentById(R.id.map) as SupportMapFragment
                        mapFragment.getMapAsync(this)
                    }
                }
                is Results.Error -> {
                    loading.dismiss()
                    showMessage(this, it.error)
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        for(location in locationList) {
            val latLng = LatLng(location.lat, location.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(location.name))
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(6.2088, 106.8456)))
    }
}