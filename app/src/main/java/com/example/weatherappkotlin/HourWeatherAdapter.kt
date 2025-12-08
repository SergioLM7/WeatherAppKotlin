package com.example.weatherappkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappkotlin.model.HourWeather

class HourWeatherAdapter (
    private val items: MutableList<HourWeather> = mutableListOf()
) : RecyclerView.Adapter<HourWeatherAdapter.VH>(){

    class VH(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
    }

    //Cada vez que entra un dato nuevo que es suceptible de aparecer en la lista
    //hacemos que se infle (que lo mandemos a la vista)
    //Crea una vista, pero aún no está la información
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view = LayoutInflater
            .from(parent.context) //contexto de la clase padre
            .inflate(R.layout.activity_hour, parent, false)

        return VH(view)
    }

    //Función que carga la información de cada item en la vista
    override fun onBindViewHolder(holder: VH, position: Int) {
        //Saco el item de la posición position de la lista
        val item = items[position]

        holder.ivIcon.setImageResource(item.iconRes)
        holder.tvHour.text = item.hour
        holder.tvTemp.text = "${item.tempC}ºC"
    }

    //Cuántos elementos van a aparecer en la vista
    override fun getItemCount(): Int = items.size

    //Me permite reemplazar la lista que hayamos cargado previamente
    //1º limpiará la lista; 2ºmetemos todos los items nuevos en la lista
    //3º notificamos que ha habido un cambio en los datos y así se vuelve a inflar nuestra vista
    fun summitList(newItems:List<HourWeather>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }



}