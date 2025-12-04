package com.example.ecotec.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.HistorialItem

class CleanHistorialAdapter(
    private val lista: List<HistorialItem>
) : RecyclerView.Adapter<CleanHistorialAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bote: TextView = v.findViewById(R.id.histBote)
        val ubicacion: TextView = v.findViewById(R.id.histUbicacion)
        val nivel: TextView = v.findViewById(R.id.histNivel)
        val fecha: TextView = v.findViewById(R.id.histFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val item = lista[pos]

        h.bote.text = "Bote: ${item.bote_id}"
        h.ubicacion.text = "Ubicación: ${item.ubicacion}"
        h.nivel.text = "Nivel previo: ${item.nivel_previo}%"
        h.fecha.text = "Fecha: ${item.fecha}"
    }

    override fun getItemCount() = lista.size
}
