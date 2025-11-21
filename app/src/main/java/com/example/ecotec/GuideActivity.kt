package com.example.ecotec

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu

class GuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        // 📋 Cards de los contenedores
        val cardGlass = findViewById<CardView>(R.id.cardGlass)
        val cardMetal = findViewById<CardView>(R.id.cardMetal)
        val cardPlastic = findViewById<CardView>(R.id.cardPlastic)
        val cardOrganic = findViewById<CardView>(R.id.cardOrganic)
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)

        // 🧭 Menú superior (☰)
        menuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_map -> {
                        Toast.makeText(this, "Abriendo mapa 🗺️", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_recommend -> {
                        Toast.makeText(this, "Abriendo recomendación 💡", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_guide -> {
                        Toast.makeText(this, "Ya estás en la guía 📘", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_report -> {
                        Toast.makeText(this, "Abriendo reporte ⚠️", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_login -> {
                        Toast.makeText(this, "Iniciando sesión 🚪", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        // ♻️ Eventos para los contenedores
        cardGlass.setOnClickListener {
            showDetailDialog(
                title = "VIDRIO",
                colorRes = R.color.green_glass,
                items = listOf(
                    Pair("🍾", "Botellas de vidrio"),
                    Pair("🫙", "Frascos"),
                    Pair("🏺", "Envases de vidrio")
                )
            )
        }

        cardMetal.setOnClickListener {
            showDetailDialog(
                title = "METALES",
                colorRes = R.color.gray_metal,
                items = listOf(
                    Pair("🥫", "Latas de aluminio"),
                    Pair("🥫", "Latas de conserva"),
                    Pair("📄", "Papel aluminio")
                )
            )
        }

        cardPlastic.setOnClickListener {
            showDetailDialog(
                title = "PLÁSTICO",
                colorRes = R.color.yellow_plastic,
                items = listOf(
                    Pair("🧴", "Botellas PET"),
                    Pair("🛍️", "Bolsas limpias"),
                    Pair("🧴", "Envases plásticos")
                )
            )
        }

        cardOrganic.setOnClickListener {
            showDetailDialog(
                title = "ORGÁNICO",
                colorRes = R.color.red_organic,
                items = listOf(
                    Pair("🍎", "Restos de comida"),
                    Pair("🍌", "Cáscaras de fruta"),
                    Pair("🥕", "Restos de verdura")
                )
            )
        }
    }

    // 💬 Función para mostrar el diálogo de información
    private fun showDetailDialog(title: String, colorRes: Int, items: List<Pair<String, String>>) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_waste_detail, null)

        val header = dialogView.findViewById<LinearLayout>(R.id.header)
        val titleText = dialogView.findViewById<TextView>(R.id.title)
        val container = dialogView.findViewById<LinearLayout>(R.id.itemContainer)
        val closeBtn = dialogView.findViewById<TextView>(R.id.closeBtn)

        header.setBackgroundColor(getColor(colorRes))
        titleText.text = title

        // 🔄 Llena dinámicamente los ítems del contenedor
        for ((emoji, label) in items) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_waste, null)
            itemView.findViewById<TextView>(R.id.emoji).text = emoji
            itemView.findViewById<TextView>(R.id.label).text = label
            container.addView(itemView)
        }

        val dialog = AlertDialog.Builder(this, R.style.CustomDialog)
            .setView(dialogView)
            .create()

        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
