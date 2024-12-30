package com.example.weatherapp.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.WeatherResponse

class WeatherRepository {

//    private val apiKey = "bf1b7be061b16df18a7be67226dff9e3"
    private val apiKey = BuildConfig.API_KEY
    suspend fun getWeatherRepo(city: String): WeatherResponse {
        val response = RetrofitInstance.api.getWeatherApi(city, apiKey)
        return response
    }
}
