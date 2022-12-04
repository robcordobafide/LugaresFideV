package com.isc.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.isc.lugares.databinding.LugarFilaBinding
import com.isc.lugares.model.Lugar
import com.isc.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter: RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //Se define la lista que se usa para mostrar la información en el recicler
    private var listaLugares = emptyList<Lugar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        //Genera una "vista" de lo creado en lugar_fila.xml (una cajita)
        val itemBinding = LugarFilaBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return LugarViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        //Se "extrae" el lugar de la lista.. para dibujarlo
        val lugar=listaLugares[position]

        //Efectivamente se dibuja la información...
        holder.bind(lugar)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setData(lugares: List<Lugar>) {
        listaLugares = lugares
        notifyDataSetChanged()
    }

    //Esta clase interna se usa para "dibujar" la información de un lugar
    //en una "caja" para cada fila de la lista/o tabla SQLite
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding):
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(lugar:Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.vistaFila.setOnClickListener {
                //Genero la acción de pasarse al update con el objeto lugar...
                val action = LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                //Efectivamente se pasa...
                itemView.findNavController().navigate(action)
            }
        }
    }

}