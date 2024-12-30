package com.example.weatherapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.location.MyLocation
import com.example.weatherapp.viewmodel.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    lateinit var temp: TextView
    lateinit var weatherCondition: TextView
    lateinit var cityName: TextView
    lateinit var searchView: EditText
    lateinit var getWeather: ImageView
    lateinit var humidity: TextView
    lateinit var pressure: TextView
    lateinit var feels: TextView
    lateinit var visibility: TextView
    lateinit var wind: TextView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: MyLocation


    lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContentView(R.layout.demo)
        temp = findViewById(R.id.currentTemp)
        weatherCondition = findViewById(R.id.weatherCondition)
        searchView = findViewById(R.id.searchView)
        getWeather = findViewById(R.id.fetchWeatherButton)
        cityName = findViewById(R.id.cityName)
        humidity = findViewById(R.id.humidityPercentage)
        pressure = findViewById(R.id.pressureNum)
        feels = findViewById(R.id.feelLikeTemp)
        visibility = findViewById(R.id.visibilityNum)
        wind = findViewById(R.id.eneForce)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)




        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        myLocation = MyLocation(this, viewModel)
        viewModel.weatherLiveData.observe(this@MainActivity, Observer { WeatherResponse ->
            Log.d("dataa", WeatherResponse.toString())

            if (WeatherResponse != null) {
//                Log.d("dataaa",WeatherResponse.weather.isNotEmpty().toString())
                if (WeatherResponse.weather.isNotEmpty()) {
                    temp.text = "${WeatherResponse.main.temp}°C"
                    weatherCondition.text = WeatherResponse.weather[0].description
                    cityName.text = WeatherResponse.name
                    humidity.text = "${WeatherResponse.main.humidity} %"

                    pressure.text = "${WeatherResponse.main.pressure} hPa"

                    feels.text = "${WeatherResponse.main.feels_like}°C"

                    val visibilityKm = WeatherResponse.visibility / 1000

                    visibility.text = "$visibilityKm km"
                    wind.text = "${WeatherResponse.wind.speed}"
                }
            }

        })


        // Get the current location of the device
        if (isInternetAvailable()) {
            myLocation.getCurrentLocation()
        } else {

            Toast.makeText(this, "No intenet connection", Toast.LENGTH_SHORT).show()
            showNoInternetDialogBox()
        }

        swipeRefreshLayout.setOnRefreshListener {


            Toast.makeText(this, "Refreshing weather data...", Toast.LENGTH_SHORT).show()

            if (isInternetAvailable()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    // Stop the refresh animation after 3 seconds
                    myLocation.getCurrentLocation()
                    swipeRefreshLayout.isRefreshing = false
                }, 3000)

            } else {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this, "No intenet connection", Toast.LENGTH_SHORT).show()
                showNoInternetDialogBox()
            }
        }

        getWeather.setOnClickListener {

            val query = searchView.text
            if (isInternetAvailable()) {
                if (query.isNotEmpty()) {
                    viewModel.getWeatherViewModel("$query")
                } else {
                    Toast.makeText(this, "plz enter the city", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                showNoInternetDialogBox()
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        return activeNetwork?.isConnected == true
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    }

    fun showNoInternetDialogBox() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("your internet is Off. please On it")
        builder.setTitle("No internet connection")
        builder.setCancelable(false)
        builder.setPositiveButton("On") { diallog, which ->
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,

        ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        myLocation.onRequestPermissionsResultMyLocation(requestCode, permissions, grantResults)
    }


    override fun onRestart() {
        super.onRestart()

        Log.d("dataa", "onrestart")
        if (isInternetAvailable()) {
            myLocation.getCurrentLocation()
        } else {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_SHORT).show()
            showNoInternetDialogBox()
        }
    }
}




