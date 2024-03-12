package com.example.weatherearth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherearth.model.RetrofitBuilder
import com.example.weatherearth.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {


        //private val weatherList=MutableLiveData<List<WeatherResponse>>()

       val weatherData:MutableLiveData<WeatherResponse> = MutableLiveData()


        private val apiService = RetrofitBuilder.api


        fun getWeather(postCode:String, apiKey:String){
                viewModelScope.launch {
                        try {
                            val response= apiService.fetchPost(postCode,apiKey)


                            weatherData.value= response
                        }catch (e:Exception){
                                e.printStackTrace()
                        }

                    
                }


        }

}