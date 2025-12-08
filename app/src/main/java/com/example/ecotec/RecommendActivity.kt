package com.example.ecotec

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.RecommendationAdapter
import com.example.ecotec.models.RecoItem

class RecommendActivity : AppCompatActivity() {

    private lateinit var spinnerUbicacion: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var menuIcon: ImageView

    // TARJETAS (paso 1)
    private lateinit var cardPlastico: LinearLayout
    private lateinit var cardPapel: LinearLayout
    private lateinit var cardMetal: LinearLayout
    private lateinit var cardOrganico: LinearLayout
    private lateinit var cardGeneral: LinearLayout

    private var tipoSeleccionado: String? = null // <- PASO 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        // REFERENCIAS
        spinnerUbicacion = findViewById(R.id.spinnerUbicacion)
        recycler = findViewById(R.id.recyclerRecomendaciones)
        menuIcon = findViewById(R.id.menuIcon)

        cardPlastico = findViewById(R.id.cardPlastico)
        cardPapel = findViewById(R.id.cardPapel)
        cardMetal = findViewById(R.id.cardMetal)
        cardOrganico = findViewById(R.id.cardOrganico)
        cardGeneral = findViewById(R.id.cardGeneral)

        recycler.layoutManager = LinearLayoutManager(this)

        // OPCIONES DE UBICACIÓN
        val ubicaciones = listOf(
            "Centro de Cómputo",
            "Cafetería",
            "Edificio K",
            "Edificio G",
            "Edificio L"
        )

        spinnerUbicacion.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            ubicaciones
        )

        spinnerUbicacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                calcularRecomendaciones()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // MENÚ SUPERIOR
        menuIcon.setOnClickListener { showMenu() }

        // LISTENERS DE TARJETAS (PASO 1)
        cardPlastico.setOnClickListener { seleccionarTipo("Plástico", cardPlastico) }
        cardPapel.setOnClickListener { seleccionarTipo("Papel", cardPapel) }
        cardMetal.setOnClickListener { seleccionarTipo("Metal", cardMetal) }
        cardOrganico.setOnClickListener { seleccionarTipo("Orgánico", cardOrganico) }
        cardGeneral.setOnClickListener { seleccionarTipo("General", cardGeneral) }
    }

    private fun showMenu() {
        val popupMenu = PopupMenu(this, menuIcon)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.show()
    }

    // ⭐ VISUAL: Tarjeta seleccionada cambia color
    private fun seleccionarTipo(tipo: String, tarjeta: LinearLayout) {
        tipoSeleccionado = tipo

        val todas = listOf(cardPlastico, cardPapel, cardMetal, cardOrganico, cardGeneral)

        // Reiniciar colores
        todas.forEach { it.setBackgroundResource(R.drawable.bg_card_white) }

        // Seleccionada
        tarjeta.setBackgroundResource(R.drawable.bg_role_selected_green)

        calcularRecomendaciones()
    }

    // ⭐ LÓGICA PRINCIPAL
    private fun calcularRecomendaciones() {

        if (tipoSeleccionado == null) {
            return // Aún no elige tipo → no recomendamos
        }

        val userPlace = spinnerUbicacion.selectedItem.toString()

        // MATRIZ DE DISTANCIAS ENTRE EDIFICIOS
        val distancias = mapOf(
            "Centro de Cómputo" to mapOf(
                "Centro de Cómputo" to 0,
                "Cafetería" to 2,
                "Edificio K" to 2,
                "Edificio G" to 3,
                "Edificio L" to 4
            ),
            "Cafetería" to mapOf(
                "Centro de Cómputo" to 2,
                "Cafetería" to 0,
                "Edificio K" to 1,
                "Edificio G" to 2,
                "Edificio L" to 4
            ),
            "Edificio K" to mapOf(
                "Centro de Cómputo" to 2,
                "Cafetería" to 1,
                "Edificio K" to 0,
                "Edificio G" to 1,
                "Edificio L" to 3
            ),
            "Edificio G" to mapOf(
                "Centro de Cómputo" to 3,
                "Cafetería" to 2,
                "Edificio K" to 1,
                "Edificio G" to 0,
                "Edificio L" to 3
            ),
            "Edificio L" to mapOf(
                "Centro de Cómputo" to 4,
                "Cafetería" to 4,
                "Edificio K" to 3,
                "Edificio G" to 3,
                "Edificio L" to 0
            )
        )

        // *** AQUÍ IRÁ TU API REAL: niveles por tipo ***
        // Por ahora seguimos con tu formato pero DINÁMICO
        val botes = listOf(
            RecoItem("Edificio K – PB", "Plástico", 60, distancias[userPlace]!!["Edificio K"]!!),
            RecoItem("Cafetería – PB", "Orgánico", 76, distancias[userPlace]!!["Cafetería"]!!),
            RecoItem("Centro de Cómputo – PB", "General", 31, distancias[userPlace]!!["Centro de Cómputo"]!!),
            RecoItem("Edificio G – PB", "Plástico", 45, distancias[userPlace]!!["Edificio G"]!!),
            RecoItem("Edificio L – PB", "Papel", 82, distancias[userPlace]!!["Edificio L"]!!)
        )

        // FILTRAR POR TIPO SELECCIONADO ⭐
        val filtrados = botes.filter { it.tipoResiduo == tipoSeleccionado }

        // SI NO HAY BOTEs EXACTOS → mostrar alternativas del mismo edificio
        val resultadoFinal = if (filtrados.isNotEmpty()) {
            filtrados.sortedBy { it.distancia }
        } else {
            botes.sortedBy { it.distancia }
        }

        recycler.adapter = RecommendationAdapter(resultadoFinal)
    }
}
