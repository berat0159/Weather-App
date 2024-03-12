package com.example.weatherearth.model

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun fetchPost(
        @Query("q") name : String,
        @Query("appid") apiKey :String



    ): WeatherResponse


}



data class WeatherResponse(
    val name: String,
    val timeZone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val main: Main,
    val sys: Sys,
    val coord:Coord
)



data class Weather(
    val id:Int,
    val main:String,
    val description:String,
    val icon:String
)

data class Main(
    val temp:Double
)

data class Sys(
    val country:String
)

data class Coord(
    val lon:Double,
    val lat:Double
)