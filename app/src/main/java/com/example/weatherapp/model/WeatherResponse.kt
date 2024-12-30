package com.example.weatherapp.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String,
    val visibility:Int,
    val wind: Wind


)

data class Main(
    val temp: Float,
    val humidity: Int,
    val pressure:Int,
    val feels_like:Float
)

data class Weather(
    val description: String
)

data class Wind(
    val speed: Float
)
