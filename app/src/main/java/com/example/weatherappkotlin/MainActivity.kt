package com.example.weatherappkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappkotlin.model.HourWeather

//Los dos puntos implican que la clase "extiende de" AppCompactActivity (los paréntesis son el constructor vacío)
class MainActivity : AppCompatActivity() {

    //Vector de números para probar forEach
    val numeros = arrayOf(1, 3, 5, 6, 7)


    private fun recorreNumeros(numeros : Array<Int>) {
        numeros.forEach { num ->
            if(esPar(num))
                println("$num es par")
            else
                println("$num es impar")

        }
    }

    //No hay que poner el tipo de dato que va a devolver la función como en Java
    //No necesita llaves porque es una función de una única línea (abreviada); si no, habría {} y return
    //Parámetro de entrada n de tipo Int
    private fun esPar(n : Int) = n % 2 == 0

    //Al no ser abreviada SI hay que indicarle el tipo del parámetro de salida después de :
    //Función que suma los números de 1 a n (incluido)
    private fun sumaHasta(n: Int) : Int {
        var total = 0

        //bucle lineal desde 1 hasta n, incluido
        for(i in 1..n) {
            total += i
        }

        return total
    }

    private fun sumaHastaDe2En2(n: Int) : Int {
        var total = 0

        //bucle lineal desde 1 hasta n, incluido, pero de 2 en 2 (i+=2)
        for(i in 1 until n step 2) {
            total += i
        }

        return total
    }

    private fun sumaHastaInverso(n: Int) : Int {
        var total = 0

        //bucle lineal desde n hasta 1, incluido, pero de 2 en 2 (i+=2)
        for(i in n downTo 1 step 2) {
            total += i
        }

        return total
    }

    private fun tipoNumero(n : Int) : String {
        //El else es porque al compilador le garantizamos así que están todas las opciones cubiertas
        return when {
            n < 0 -> "Negativo"
            n == 0 -> "Cero"
            else -> "Positivo"
        }
    }

    private fun procesarLista(list: List<Int>, criterio: (Int) -> Boolean): List<Int> =
        list.filter(criterio)

    //Nos permite accceder a él desde cualquier otra clase
    //parecido al static de Java
    companion object{
        //Constante en tiempo de compilación (const) y también constante en ejecución (val)
        //No siempre es necesario el const dentro de un companion; este es un ejemplo para conocerlo
        const val TEXTO1 = "TEXTO1"
        const val TEXTO2 = "TEXTO2"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Llamada al constructor de la clase padre (AppCompatActivity)
        super.onCreate(savedInstanceState)
        //Para cargar la vista principal (layout)
        setContentView(R.layout.activity_main)

        //Al espacio R.id viajan todos los id que usas en las vistas que tengas cargadas
        //Enlazamos los items que necesitamos de la vista a nuestra lógica
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val etEntrada = findViewById<EditText>(R.id.etEntrada)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnIrSegunda = findViewById<Button>(R.id.btnSecondActivity)
        val btnCargar = findViewById<Button>(R.id.btnLoadPrediction)
        //Otra forma de enlazar un elemento de una vista
        val btnGPS: Button = findViewById(R.id.btnGPS)
        val btnAccelerometer: Button = findViewById(R.id.btnAccelerometer)
        val rv = findViewById<RecyclerView>(R.id.rvHours)

        //Si cambiamos cualquier propiedad de estas variables, se cambia la propiedad del xml
        tvTitulo.text = "KOTLIN DEMO"

        val adapter = HourWeatherAdapter()
        //EL gestor de nuestro rv será un LinearLayout y se va a cargar en el contexto de nuestro Main
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        btnCargar.setOnClickListener {
            val datos = listOf(
                HourWeather("9:00", 18, R.drawable.ic_nublado),
                HourWeather("10:00", 21, R.drawable.ic_nublado),
                HourWeather("12:00", 22, R.drawable.ic_sol),
                HourWeather("14:00", 21, R.drawable.ic_nublado))

            adapter.summitList(datos)
        }

        btnIrSegunda.setOnClickListener {
            val textoEntrada = etEntrada.text.toString().trim()
            val numero = textoEntrada.toIntOrNull()

            //el apply es lo que lanza la ventana como tal
            val intent = Intent(this, SecondActivity::class.java).apply {
                //Va a servir para pasar 1 variable a la SecondActivity (vista)
                // No pasar parámetros a machete (magic quotes), siempre con constantes
                // si quieres pasar más variables, puedes usar n putExtra
                putExtra(TEXTO1, textoEntrada)

                if(numero != null) putExtra(TEXTO2, numero)
            }
            startActivity(intent)
        }

        btnProcesar.setOnClickListener {
            //Lo que escriba el usuario sin el espacio inicial ni final
            val valor = etEntrada.text.toString().trim()

            if(valor.isEmpty()) {
                etEntrada.error = "Escribe algo primero"
                tvResultado.text = ""
                return@setOnClickListener
            }

            //Si el usuario ha escrito cualquier cosa que no sea un int, será null
            val num = valor.toIntOrNull()
            val salida = if(num != null) {

                buildString {
                    appendLine("Entrada = $num")
                    appendLine("Tipo número = ${tipoNumero(num)}")
                    appendLine("Suma hasta = ${sumaHasta(num)}")
                    appendLine("Es par = ${esPar(num)}")
                }
            } else {
                buildString {
                    appendLine("Entrada = $valor")
                    appendLine("Longitud = ${valor.length}")
                }
            }

            tvResultado.text = salida
        }

        btnGPS.setOnClickListener {
            //La forma de lanzar una Activity es a través de un intent
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        btnAccelerometer.setOnClickListener {
            val intent = Intent(this, AccelerometerActivity::class.java)
            startActivity(intent)
        }

    }

}