package com.example.weatherappkotlin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocationActivity: AppCompatActivity(), LocationListener {

    //lateinit -> es una forma de declarar una variable que todavía no tiene valor
    //pero te comprometes a inicializarla después
    private lateinit var tvLatLong: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnVolverLocation: Button

    //Intermediario entre el programa y el GPS
    private lateinit var locationManager: LocationManager

    // Mapa de OpenStreetMap (osmdroid)
    private lateinit var mapView: MapView
    private lateinit var mapMarker: Marker
    private lateinit var mapController: IMapController

    //Cuando hacemos una petición al SO de Android para que nos envíe datos de un sensor
    // va a una pila de pticiones y se asigna un código de petición
    // cuando llega la respuesta, Android le asocia ese código y nos lo devuelve (si coincide el código con otra petición,
    // automáticamente lo reasigna)
    private val LOCATION_REQUEST_CODE = 1001


    //ONCREATE para enlazar con la vista (se ejecuta al abrir la Activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración básica de osmdroid: user agent (recomendado)
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_location)

        tvLatLong = findViewById(R.id.tvLatLong)
        tvStatus = findViewById(R.id.tvStatus)
        btnVolverLocation = findViewById<Button>(R.id.btnVolverLocation)

        // Vinculamos el MapView del layout
        mapView = findViewById(R.id.mapView)
        // Permitimos zoom con dos dedos, etc.
        mapView.setMultiTouchControls(true)

        // Establecemos un zoom inicial para el mapa
        mapController = mapView.controller
        mapController.setZoom(16.0)
        // Centramos en una posición inicial (por ejemplo, Madrid)
        val startPoint = GeoPoint(40.4168, -3.7038)
        mapController.setCenter(startPoint)

        // Creamos un marcador inicial
        mapMarker = Marker(mapView)
        mapMarker.position = startPoint
        mapMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapMarker.title = "Tu posición"
        mapView.overlays.add(mapMarker)

        //Inicializamos el servicio de localización
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        //Comprobar permisos
        checkPermissionsAndStart()

        btnVolverLocation.setOnClickListener {
            finish()
        }
    }


    //Checkear permisos (común a cualquier app que use permisos)
    private fun checkPermissionsAndStart() {
        //Comprobar si el permiso se ha concedido
        // comparando si el resultado de consultar si nuestra activity tiene permisos para la localización fina (GPS)
        // es igual a PackageManager.PERMISSION_GRANTED
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if(!hasPermission) {
            //Sacamos una ventana al usuario para pedirle permisos de GPS
            // OJO: ActivityCompat espera un vector de permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        } else {
            //Iniciar GPS
            startLocationUpdates()
        }
    }

    //STARTLOCATION -> va a leer las coordenadas del dispositivo
    //Suppres(MissingPermission) va a revisar que se han concedido los permisos antes de ejecutar el código de la función
    //En el caso de no hacerlo, el algoritmo de Google nos bloquearía la aplicación de no hacerlo
    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        tvStatus.text = "Esperando localización..."

        //Es una instancia de la clase LocationManager que nos permite acceder a la localización del GPS del dispositivo
        // Parámetros: 1.el proveedor/sensor de la localización (GPS),
        // 2. milisegundos(L) en los que se refrescará la ubicación si no se mueve el dispositivo,
        // 3. La mínima distancia (en m) a partir de la cual se actualizará la ubicación, 4.Contexto
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L, //cada 2 segundos
            0f, //sin distancia mínima
            this //esta Activity será la que implemente el locationManager
        )
    }

    //Saca la longitud y la latitud para enviarlas a la view cada vez que el GPS envía nuevas coordenadas
    //El parámetro nos lo dará el GPS cada vez que cambie, no se la meteremos a mano
    override fun onLocationChanged(location: Location) {

        var lat = location.latitude
        var lon = location.longitude

        tvLatLong.text = buildString {
            append("Latitud: ")
            append(lat)
            append("\nLongitud: ")
            append(lon)
        }
        tvStatus.text = "Localización recibida"

        // Convertimos la localización a GeoPoint de osmdroid
        val newPoint = GeoPoint(lat, lon)
        // Movemos el mapa al nuevo punto
        mapController = mapView.controller
        mapController.setCenter(newPoint)

        // Movemos el marcador a la nueva posición
        mapMarker.position = newPoint

        // Forzamos repintado del mapa
        mapView.invalidate()

        //Mensaje tipo toast
        Toast.makeText(this, "Localización recibida", Toast.LENGTH_SHORT).show()
    }


    //Metodo del ciclo de vida de manejo de permisos solicitados.
    // Android lo llama cuando el usuario haga click en aceptar/cancelar permisos
    //gran results -> 0: permiso aceptado; 1: permiso denegado
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //Permiso aceptado
        if(requestCode == LOCATION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            tvStatus.text = "Permisos de localización DENEGADOS"
            Toast.makeText(this, "Permisos de localización DENEGADOS", Toast.LENGTH_SHORT).show()
        }
    }

    //Usamos estos métodos del ciclo de vida para revisar los permisos y parar la actualización del GPS
    //porque son más eficientes a nivel de batería y más robustos
    override fun onResume() {
        super.onResume()
        mapView.onResume() // Necesario para el mapa
        checkPermissionsAndStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Necesario para el mapa
        locationManager.removeUpdates(this)
    }

}