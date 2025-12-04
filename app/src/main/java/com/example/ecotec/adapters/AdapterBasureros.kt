package com.example.ecotec.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.Basurero

class AdapterBasureros(
    private val lista: List<Basurero>,
    private val onClick: (Basurero) -> Unit
) : RecyclerView.Adapter<AdapterBasureros.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtId: TextView = v.findViewById(R.id.txtId)
        val txtUbicacion: TextView = v.findViewById(R.id.txtUbicacion)
        val txtEstado: TextView = v.findViewById(R.id.txtEstado)
        val progreso: ProgressBar = v.findViewById(R.id.progreso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_basurero, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(h: ViewHolder, i: Int) {
        val b = lista[i]
        h.txtId.text = b.bote_id
        h.txtUbicacion.text = b.ubicacion
        h.progreso.progress = b.nivel
        h.txtEstado.text = "Estado: ${b.estado}"

        h.itemView.setOnClickListener { onClick(b) }
    }

    override fun getItemCount() = lista.size
}
