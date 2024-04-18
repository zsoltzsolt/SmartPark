package com.example.smartpark.ui.main.fragments

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.smartpark.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val uptLocation = LatLng(45.7475, 21.2262) 

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(uptLocation).title("Universitatea Politehnica Timișoara"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uptLocation, 17f))
        setupLocationUpdates()
    }

    private fun setupLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
                    fetchDirections(getDirectionsUrl(currentLocation, uptLocation))
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val parameters = "origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&key=${getString(R.string.google_maps_key)}"
        return "https://maps.googleapis.com/maps/api/directions/json?$parameters"
    }

    private fun fetchDirections(urlString: String) {
        val thread = Thread {
            try {
                val url = URL(urlString)
                val conn = url.openConnection() as HttpsURLConnection
                conn.requestMethod = "GET"
                val inputStream = conn.inputStream
                val allText = inputStream.bufferedReader().use { it.readText() }
                activity?.runOnUiThread {
                    parseDirections(allText)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    private fun parseDirections(jsonData: String) {
        val jsonObject = JSONObject(jsonData)
        val routes = jsonObject.getJSONArray("routes")
        if (routes.length() > 0) {
            val route = routes.getJSONObject(0)
            val polyline = route.getJSONObject("overview_polyline").getString("points")
            val decodedPath = PolyUtil.decode(polyline)
            activity?.runOnUiThread {
                drawPolyline(decodedPath)
            }
        }
    }

    private fun drawPolyline(path: List<LatLng>) {
        mMap.clear()  // Clear previous markers or paths
        mMap.addPolyline(PolylineOptions().addAll(path).width(12f).color(Color.BLUE).geodesic(true))
        mMap.addMarker(MarkerOptions().position(uptLocation).title("UPT"))
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (::fusedLocationClient.isInitialized && ::locationCallback.isInitialized) {
            setupLocationUpdates()
        }
    }
}
