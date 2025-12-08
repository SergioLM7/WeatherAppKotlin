package com.example.weatherappkotlin.model

data class HourWeather (
    val hour: String,
    val tempC: Int,
    val iconRes: Int
)