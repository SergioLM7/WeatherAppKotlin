package com.example.weatherappkotlin

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LocationActivity: AppCompatActivity(), LocationListener {

    //lateinit -> es una forma de declarar una variable que todavía no tiene valor
    //pero te comprometes a inicializarla después
    private lateinit var tvLatLong: TextView
    private lateinit var status: TextView
    //Intermediario entre el programa y el GPS
    private lateinit var locationManager: LocationManager

    private val LOCATION_REQUEST_CODE = 1001


    //ONCREATE para enlazar con la vista

    //Checkear permisos (común a cualquier app que use permisos)

    //STARTLOCATION -> va a leer y a mostrar los mensajes

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

}