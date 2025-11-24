package com.example.ecotec

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.content.Intent
import com.google.android.material.snackbar.Snackbar

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerTipo)
        val spinnerContenedor = findViewById<Spinner>(R.id.spinnerContenedor)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)
        val layoutFoto = findViewById<LinearLayout>(R.id.layoutFoto)

        // --- Configurar menú superior ---
        menuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_map -> {
                        startActivity(Intent(this, MapActivity::class.java))
                        true
                    }
                    R.id.action_recommend -> {
                        startActivity(Intent(this, RecommendActivity::class.java))
                        true
                    }
                    R.id.action_guide -> {
                        startActivity(Intent(this, GuideActivity::class.java))
                        true
                    }
                    R.id.action_report -> {
                        Toast.makeText(this, "Ya estás en Reportar ⚠️", Toast.LENGTH_SHORT).show()
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

        // --- Datos de ejemplo para los spinners ---
        val tiposProblema = arrayOf(
            "Seleccionar tipo",
            "Tapa atascada o rota",
            "Sensor no funciona",
            "Contenedor desbordado",
            "Cámara no identifica residuos",
            "Otro problema"
        )

        val contenedores = arrayOf(
            "Seleccionar contenedor",
            "Edificio L - Planta baja",
            "Edificio L - Planta alta",
            "Edificio G - Planta baja",
            "Edificio G - Planta alta",
            "Edificio K - Planta baja",
            "Edificio K - Planta alta",
            "Cafetería",
            "Centro de Cómputo"
        )

        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposProblema)
        spinnerContenedor.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, contenedores)

        // --- Click para seleccionar foto (sin funcionalidad aún) ---
        layoutFoto.setOnClickListener {
            Snackbar.make(it, "Función de subir imagen próximamente 📷", Snackbar.LENGTH_SHORT).show()
        }

        // --- Botón Enviar ---
        btnEnviar.setOnClickListener {
            val tipo = spinnerTipo.selectedItem.toString()
            val contenedor = spinnerContenedor.selectedItem.toString()
            val descripcion = edtDescripcion.text.toString()

            if (tipo == "Seleccionar tipo" || contenedor == "Seleccionar contenedor" || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Reporte enviado correctamente ✅", Toast.LENGTH_LONG).show()
                edtDescripcion.text.clear()
                spinnerTipo.setSelection(0)
                spinnerContenedor.setSelection(0)
            }
        }
    }
}
