package com.example.weatherappkotlin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        //Capturar lo que nos envían desde la MainActivity
        val textoRecibido = intent.getStringExtra(MainActivity.TEXTO1) ?: ""
        //getIntExtra necesita de un valor por default como segundo parámetro porque no contempla recibir un NULL
        //el Default sirve para saber si hemos recibido número o NO
        val numeroRecibido = intent.getIntExtra(MainActivity.TEXTO2, Int.MIN_VALUE)

        val resumen = buildString {
            appendLine("Info recibida de la MainActivity: ")
            appendLine("---------------------------")
            appendLine("---------------------------")
            appendLine("Texto recibido: $textoRecibido")
            if(numeroRecibido != Int.MIN_VALUE) {
                appendLine("Número recibido: $numeroRecibido")
            } else {
                appendLine("No se ha recibido ningún número.")
            }
        }

        tvInfo.text = resumen

        btnVolver.setOnClickListener {
            finish()
        }
    }

}