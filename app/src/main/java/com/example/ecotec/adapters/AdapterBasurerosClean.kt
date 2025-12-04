package com.example.ecotec.adapters

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.Basurero

class AdapterBasurerosClean(
    private val lista: List<Basurero>,
    private val onClick: (Basurero) -> Unit
) : RecyclerView.Adapter<AdapterBasurerosClean.Holder>() {

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.itemCleanId)
        val txtUbicacion: TextView = view.findViewById(R.id.itemCleanUbicacion)
        val txtNivel: TextView = view.findViewById(R.id.itemCleanNivel)
        val btnAtender: Button = view.findViewById(R.id.btnAtender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_basurero_clean, parent, false)
        return Holder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val b = lista[position]

        holder.txtId.text = "ID: ${b.bote_id}"
        holder.txtUbicacion.text = b.ubicacion
        holder.txtNivel.text = "Nivel: ${b.nivel}%"

        holder.btnAtender.setOnClickListener {
            onClick(b)
        }
    }
}
