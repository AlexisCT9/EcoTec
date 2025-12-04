package com.example.ecotec.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.Alerta

class AlertasAdapter(
    private val data: List<Alerta>,
    private val onClick: (Alerta) -> Unit
) : RecyclerView.Adapter<AlertasAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView = view.findViewById(R.id.txtTipo)
        val txtMensaje: TextView = view.findViewById(R.id.txtMensaje)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)

        fun bind(a: Alerta) {
            txtTipo.text = a.tipo_notificacion
            txtMensaje.text = a.mensaje
            txtFecha.text = a.fecha

            itemView.setOnClickListener { onClick(a) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
