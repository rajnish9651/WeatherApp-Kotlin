package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository()

    private val weatherMutableLiveData = MutableLiveData<WeatherResponse?>()
    val weatherLiveData: LiveData<WeatherResponse?> = weatherMutableLiveData


    fun getWeatherViewModel(city: String) {


//        viewModelScope.launch {
//            val response = async (Dispatchers.IO){  weatherRepository.getWeatherRepo(city) }.await()
//                if (response.weather.isNotEmpty()) {
//                    weatherMutableLiveData.postValue(response)
//                }
//
//        }

        viewModelScope.async {
            val response = weatherRepository.getWeatherRepo(city)

                weatherMutableLiveData.postValue(response)

        }.onAwait

    }

}