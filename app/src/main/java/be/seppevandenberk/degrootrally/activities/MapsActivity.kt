package be.seppevandenberk.degrootrally.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityMapsBinding
import be.seppevandenberk.degrootrally.model.WeatherData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, android.location.LocationListener,
    SensorEventListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private val client = OkHttpClient()
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private var humiditySensor: Sensor? = null
    private var pressureSensor: Sensor? = null
    private lateinit var windTextView: TextView
    private lateinit var windButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        }

        windTextView = findViewById(R.id.windSpeedTextView)
        windButton = findViewById(R.id.wind_data_button)

        windButton.setOnClickListener {
            updateWind()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
        } else {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let { updateCurrentLocation(it) }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        getCurrentLocationUser()
    }

    private fun updateCurrentLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (temperatureSensor != null && event.sensor == temperatureSensor) {
                val temperature = event.values[0] // Temperature value in Celsius
                this.findViewById<TextView>(R.id.temperatureTextView).text =
                    "Temperature: $temperature °C"
            } else if (humiditySensor != null && event.sensor == humiditySensor) {
                val humidity = event.values[0] // Humidity value in percentage
                this.findViewById<TextView>(R.id.humidityTextView).text =
                    "Humidity: $humidity %"
            } else if (pressureSensor != null && event.sensor == pressureSensor) {
                val pressure = event.values[0] // Pressure value in hPa (millibar)
                this.findViewById<TextView>(R.id.pressureTextView).text =
                    "Pressure: $pressure hPa"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onLocationChanged(location: Location) {
    }

    private fun updateWind() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        getWindData("https://api.open-meteo.com/v1/forecast?latitude=" + lastKnownLocation.latitude.toString() + "&longitude=" + lastKnownLocation.longitude.toString() + "&hourly=windspeed_10m,winddirection_10m&daily=weathercode&current_weather=true&forecast_days=1&timezone=auto")
    }

    private fun getWindData(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val strResponse = response.body()!!.string()

                val gson = Gson()
                val weatherData = gson.fromJson(strResponse, WeatherData::class.java)
                val currentWeather = weatherData.current_weather

                runOnUiThread {
                    windTextView.text = "Wind: " + currentWeather.windspeed.toString() + " km/h"
                    findViewById<ImageView>(R.id.wind_arrow).rotation =
                        currentWeather.winddirection.toFloat()
                }
            }
        })
    }
}