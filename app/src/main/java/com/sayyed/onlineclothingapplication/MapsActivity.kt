package com.sayyed.onlineclothingapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sayyed.onlineclothingapplication.models.LatitudeLongitude

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  var listLatLon = ArrayList<LatitudeLongitude>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        loadMultipleLocation()
    }

    fun loadSingleLocation () {
        mMap.addMarker(
            MarkerOptions().position(LatLng(27.7061949, 85.3300394))
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(27.7061949, 85.3300394), 17F), 4000, null
        )
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    fun loadMultipleLocation() {

        listLatLon.add(LatitudeLongitude(27.7061949, 85.3300394, "My College"))
        listLatLon.add(LatitudeLongitude(27.70465, 85.329471, "My House"))

        for (location in listLatLon) {
            mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title(location.markerName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            )
        }

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(27.7061949, 85.3300394), 17F), 4000, null
        )
        mMap.uiSettings.isZoomControlsEnabled = true
    }
}