package com.example.pokedex.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pokedex.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import android.widget.ImageView
class GpsActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private val distanceThreshold = 10 // Umbral de distancia en metros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for (location in locationResult.locations) {
                        lastLocation?.let { previousLocation ->
                            val distance = calculateDistance(previousLocation, location)
                            if (distance >= distanceThreshold) {
                                val pokemonName = "Pikachu" // Reemplaza con la forma de obtener el nombre del Pokémon
                                showPokemonAlert(pokemonName)
                            }
                        }
                        lastLocation = location
                    }
                }
            }
        }

        if (checkLocationPermission()) {
            startLocationUpdates()
        }
    }
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Intervalo en milisegundos
            fastestInterval = 5000 // Intervalo más rápido en milisegundos
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }
    private fun calculateDistance(location1: Location, location2: Location): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            location1.latitude, location1.longitude,
            location2.latitude, location2.longitude, results
        )
        return results[0]
    }
    private fun showPokemonAlert(pokemonName: String) {
        val imageView = findViewById<ImageView>(R.id.pokemonImageView)
        imageView.visibility = View.VISIBLE
        imageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("¡Pokemon encontrado!")
        alertDialogBuilder.setMessage("Has encontrado un $pokemonName cerca.") // Mensaje con el nombre del Pokémon
        alertDialogBuilder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                // Permiso denegado, manejar el caso
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}
