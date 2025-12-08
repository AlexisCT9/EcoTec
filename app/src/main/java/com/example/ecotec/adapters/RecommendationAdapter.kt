package com.example.ecotec.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.MapActivity
import com.example.ecotec.R
import com.example.ecotec.models.RecoItem
import com.google.android.material.card.MaterialCardView

class RecommendationAdapter(
    private val lista: List<RecoItem>
) : RecyclerView.Adapter<RecommendationAdapter.RecoViewHolder>() {

    class RecoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.cardReco)
        val txtUbicacion: TextView = view.findViewById(R.id.txtUbicacion)
        val txtTipoResiduo: TextView = view.findViewById(R.id.txtTipoResiduo)
        val txtNivel: TextView = view.findViewById(R.id.txtNivel)
        val txtDistancia: TextView = view.findViewById(R.id.txtDistancia)
        val iconTipo: ImageView = view.findViewById(R.id.iconTipo)
        val iconFlecha: ImageView = view.findViewById(R.id.iconFlecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecoViewHolder, position: Int) {
        val item = lista[position]
        val ctx = holder.itemView.context

        // Texto base
        holder.txtUbicacion.text = item.ubicacion
        holder.txtTipoResiduo.text = item.tipoResiduo
        holder.txtNivel.text = ctx.getString(R.string.nivel_porcentaje, item.nivel)

        // --- DISTANCIA ---
        val textoDistancia = when (item.distancia) {
            0 -> "Aquí mismo"
            1 -> "Muy cerca"
            2 -> "Cerca"
            3 -> "Lejos"
            else -> "Muy lejos"
        }
        holder.txtDistancia.text = "Distancia: $textoDistancia"

        val colorDist = when (item.distancia) {
            0 -> R.color.eco_green
            1 -> R.color.eco_green_light
            2 -> R.color.eco_green_dark
            else -> R.color.eco_gray
        }
        holder.txtDistancia.setTextColor(ContextCompat.getColor(ctx, colorDist))

        // --- COLOR Y BORDES SEGÚN TIPO ---
        val bordeColor: Int = when (item.tipoResiduo.lowercase()) {
            "plástico", "plastico" -> {
                holder.iconTipo.setImageResource(R.drawable.ic_trash)
                R.color.eco_green
            }
            "papel / cartón", "papel", "cartón" -> {
                holder.iconTipo.setImageResource(R.drawable.ic_book)
                R.color.eco_blue
            }
            "metal" -> {
                holder.iconTipo.setImageResource(R.drawable.ic_clean)
                R.color.eco_secondary_green
            }
            "orgánico", "organico" -> {
                holder.iconTipo.setImageResource(R.drawable.ic_trash_circle)
                R.color.eco_orange
            }
            else -> {
                holder.iconTipo.setImageResource(R.drawable.ic_clean)
                R.color.eco_gray
            }
        }

        holder.iconTipo.setColorFilter(ContextCompat.getColor(ctx, bordeColor))
        holder.txtTipoResiduo.setTextColor(ContextCompat.getColor(ctx, bordeColor))

        holder.card.strokeColor = ContextCompat.getColor(ctx, bordeColor)
        holder.card.strokeWidth = 5

        // Animación scroll
        holder.card.startAnimation(
            AnimationUtils.loadAnimation(ctx, R.anim.recycler_fade_up)
        )

        // Animación flecha
        val rot = RotateAnimation(
            0f, 360f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rot.duration = 900
        holder.iconFlecha.startAnimation(rot)

        // Hover
        holder.card.setOnTouchListener { v, event ->
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(90).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(110)
            }
            false
        }

        // --- CLICK EN LA FLECHA → MANDAR DESTINO AL MAPA ---
        holder.iconFlecha.setOnClickListener {
            val intent = Intent(ctx, MapActivity::class.java)

            // Envía SOLO el edificio antes del "–"
            val destino = item.ubicacion.substringBefore("–").trim()

            intent.putExtra("destino", destino)

            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size
}
