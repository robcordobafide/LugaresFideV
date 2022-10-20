package com.lugares_v.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.lugares_v.databinding.FragmentAddLugarBinding
import com.lugares_v.databinding.LugarFilaBinding
import com.lugares_v.model.Lugar

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {
    //La lista de lugares a "dibujar"
    private var listaLugares= emptyList<Lugar>()

    //Contenedor de vistas"cajas" en memoria...
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            fun dibuja(lugar: Lugar){
                itemBinding.tvNombre.text = lugar.nombre
                itemBinding.tvTelefono.text = lugar.telefono
                itemBinding.tvCorreo.text = lugar.correo
            }
        }

    //crea una "cajita" una vista del tipo lugarFila...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context)
            ,parent
        ,false)
        return LugarViewHolder(itemBinding)
    }

    //con una "cajita" creada se pasa a dibujar los datos del lugar x
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugarActual = listaLugares[position]
        holder.dibuja(lugarActual)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setLugares(lugares : List<Lugar>){
        listaLugares = lugares
        notifyDataSetChanged()  // se notifica q el conjunto de datos cambio y se redibuja toda la lista
    }
}