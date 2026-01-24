package com.example.weatherappkotlin

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.sqrt

class AccelerometerActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvAccelValue: TextView
    private lateinit var tvAccelLevel: TextView

    //Variable para conectar con el acelerómetro
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor?= null

    private var lastAcceleration = 0f
    private var currentAcceleration = 0f
    private var accelerometerIntensity = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerometer)

        tvAccelValue = findViewById(R.id.tvAccelValue)
        tvAccelLevel = findViewById(R.id.tvAccelLevel)

        //Obtener el servicio de sensores del sistema y asignárselo a sensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Obtenemos el acelerómetro del dispositivo
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //Inicializar la aceleración inicial y la última (ojo que el acelerometro puede estar parado y recibiríamos null)
        //Por eso usamos el Gravity_Earth como valor default inicial
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            //Calculamos la aceleración según la fórmula física para calcularla
            val acceleration = sqrt(x * x + y * y + z * z)

            lastAcceleration = currentAcceleration
            currentAcceleration = acceleration

            val intensity = currentAcceleration - lastAcceleration
            accelerometerIntensity = abs(intensity)

            tvAccelValue.text = String.format("%.2f", accelerometerIntensity)

            val level = when {
                accelerometerIntensity < 1f -> "Nivel: quieto"
                accelerometerIntensity < 3f -> "Nivel: suave"
                accelerometerIntensity < 6f -> "Nivel: medio"
                else -> "Nivel: fuerte"
            }

            tvAccelLevel.text = level
        }
    }

    //Hay que controlar si esta actividad cambia a otra, qué hacemos con el sensor (desactivarlo)
    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    //Hay que controlar cuando esta actividad vuelve a estar activa, qué hacemos con el sensor (activarlo)
    override fun onResume(){
        //Para que se construya primero la clase padre de la que hereda nuestra Activity
        super.onResume()
        //Si el acelerometro no es nulo (está conectado), ejecuta el bloque después del also, sino, NO
        //El sensor de la función flecha es el propio acelerómetro
        accelerometer?.also{ sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_UI //Frecuencia de actuación (en este caso, moderada)
            )
        }
    }

    //Si el sensor cambia la sensibilidad... (es necesario implementarla por el SensorEventListener,
    //pero no la necesitamos en este caso
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}