package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // --- Menú superior (☰) ---
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_map -> {
                        Toast.makeText(this, "Ya estás en el mapa 🗺️", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_recommend -> {
                        Toast.makeText(this, "Abriendo recomendación 💡", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, RecommendActivity::class.java))
                        true
                    }
                    R.id.action_guide -> {
                        Toast.makeText(this, "Abriendo guía 📘", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, GuideActivity::class.java))
                        true
                    }
                    R.id.action_report -> {
                        Toast.makeText(this, "Abriendo reporte ⚠️", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ReportActivity::class.java))
                        true
                    }
                    R.id.action_login -> {
                        Toast.makeText(this, "Iniciando sesión 🚪", Toast.LENGTH_SHORT).show()
                        // más adelante -> startActivity(Intent(this, LoginActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        // --- Botones del mapa ---
        val btnL = findViewById<Button>(R.id.btnL)
        val btnG = findViewById<Button>(R.id.btnG)
        val btnK = findViewById<Button>(R.id.btnK)
        val btnCafeteria = findViewById<Button>(R.id.btnCafeteria)
        val btnCC = findViewById<Button>(R.id.btnCC)

        // --- Acciones de los botones ---
        btnL.setOnClickListener { showBuildingBottomSheet("Edificio L", obtenerContenedoresL()) }
        btnG.setOnClickListener { showBuildingBottomSheet("Edificio G", obtenerContenedoresG()) }
        btnK.setOnClickListener { showBuildingBottomSheet("Edificio K", obtenerContenedoresK()) }
        btnCafeteria.setOnClickListener { showBuildingBottomSheet("Cafetería", obtenerContenedoresCafeteria()) }
        btnCC.setOnClickListener { showBuildingBottomSheet("Centro de Cómputo", obtenerContenedoresCC()) }
    }

    private fun showBuildingBottomSheet(nombre: String, pisos: Map<String, List<Contenedor>>) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_building, null)

        val txtBuildingName = view.findViewById<TextView>(R.id.txtBuildingName)
        txtBuildingName.text = nombre

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerBins)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ContenedorAdapter(pisos)

        dialog.setContentView(view)
        dialog.show()
    }

    // --- Datos por planta ---
    private fun obtenerContenedoresL(): Map<String, List<Contenedor>> = mapOf(
        "PB" to listOf(
            Contenedor("Vidrio", 45, "Disponible", "#059669"),
            Contenedor("Metales", 78, "Disponible", "#9CA3AF"),
            Contenedor("Plástico", 92, "Lleno", "#F59E0B"),
            Contenedor("Orgánico", 65, "Disponible", "#EF4444"),
            Contenedor("Reciclable", 30, "Disponible", "#3B82F6")
        ),
        "P1" to listOf(
            Contenedor("Vidrio", 55, "Disponible", "#059669"),
            Contenedor("Metales", 40, "Disponible", "#9CA3AF"),
            Contenedor("Plástico", 88, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 95, "Lleno", "#EF4444"),
            Contenedor("Reciclable", 20, "Disponible", "#3B82F6")
        )
    )

    private fun obtenerContenedoresG(): Map<String, List<Contenedor>> = mapOf(
        "PB" to listOf(
            Contenedor("Vidrio", 70, "Disponible", "#059669"),
            Contenedor("Metales", 50, "Disponible", "#9CA3AF"),
            Contenedor("Plástico", 85, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 93, "Lleno", "#EF4444"),
            Contenedor("Reciclable", 60, "Disponible", "#3B82F6")
        ),
        "P1" to listOf(
            Contenedor("Vidrio", 35, "Disponible", "#059669"),
            Contenedor("Metales", 90, "Lleno", "#9CA3AF"),
            Contenedor("Plástico", 45, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 75, "Disponible", "#EF4444"),
            Contenedor("Reciclable", 50, "Disponible", "#3B82F6")
        )
    )

    private fun obtenerContenedoresK(): Map<String, List<Contenedor>> = mapOf(
        "PB" to listOf(
            Contenedor("Vidrio", 80, "Disponible", "#059669"),
            Contenedor("Metales", 95, "Lleno", "#9CA3AF"),
            Contenedor("Plástico", 60, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 40, "Disponible", "#EF4444"),
            Contenedor("Reciclable", 72, "Disponible", "#3B82F6")
        ),
        "P1" to listOf(
            Contenedor("Vidrio", 25, "Disponible", "#059669"),
            Contenedor("Metales", 68, "Disponible", "#9CA3AF"),
            Contenedor("Plástico", 91, "Lleno", "#F59E0B"),
            Contenedor("Orgánico", 55, "Disponible", "#EF4444"),
            Contenedor("Reciclable", 48, "Disponible", "#3B82F6")
        )
    )

    private fun obtenerContenedoresCafeteria(): Map<String, List<Contenedor>> = mapOf(
        "PB" to listOf(
            Contenedor("Vidrio", 88, "Disponible", "#059669"),
            Contenedor("Metales", 92, "Lleno", "#9CA3AF"),
            Contenedor("Plástico", 76, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 94, "Lleno", "#EF4444")
        )
    )

    private fun obtenerContenedoresCC(): Map<String, List<Contenedor>> = mapOf(
        "PB" to listOf(
            Contenedor("Vidrio", 15, "Disponible", "#059669"),
            Contenedor("Metales", 42, "Disponible", "#9CA3AF"),
            Contenedor("Plástico", 58, "Disponible", "#F59E0B"),
            Contenedor("Orgánico", 31, "Disponible", "#EF4444")
        )
    )
}
