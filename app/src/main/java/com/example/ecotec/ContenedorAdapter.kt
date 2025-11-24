package com.example.ecotec

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.Contenedor

class ContenedorAdapter(private val pisos: Map<String, List<Contenedor>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val pisoKeys = pisos.keys.toList()

    override fun getItemCount(): Int {
        var total = 0
        for (piso in pisoKeys) {
            total++ // título del piso
            total += pisos[piso]?.size ?: 0
        }
        return total
    }

    override fun getItemViewType(position: Int): Int {
        var index = 0
        for (piso in pisoKeys) {
            if (position == index) return 0
            index++
            val bins = pisos[piso]
            if (position < index + (bins?.size ?: 0)) return 1
            index += bins?.size ?: 0
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_floor_title, parent, false)
            PisoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contenedor, parent, false)
            ContenedorViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var index = 0
        for (piso in pisoKeys) {
            if (position == index && holder is PisoViewHolder) {
                holder.bind(piso)
                return
            }
            index++
            val bins = pisos[piso]
            if (position < index + (bins?.size ?: 0)) {
                val contenedor = bins!![position - index]
                if (holder is ContenedorViewHolder) holder.bind(contenedor)
                return
            }
            index += bins?.size ?: 0
        }
    }

    class PisoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtPiso: TextView = itemView.findViewById(R.id.txtFloor)
        fun bind(nombre: String) {
            txtPiso.text = if (nombre == "PB") "Planta Baja" else "Planta Alta"
        }
    }

    class ContenedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        private val txtNivel: TextView = itemView.findViewById(R.id.txtNivel)
        private val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        private val indicadorColor: View = itemView.findViewById(R.id.indicadorColor)

        fun bind(c: Contenedor) {
            txtNombre.text = c.nombre
            txtNivel.text = "Nivel: ${c.nivel}%"
            txtEstado.text = c.estado
            indicadorColor.setBackgroundColor(Color.parseColor(c.color))

            txtEstado.setTextColor(
                if (c.estado == "Lleno") Color.parseColor("#EF4444")
                else Color.parseColor("#059669")
            )
        }
    }
}
