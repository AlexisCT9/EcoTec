package com.example.ecotec

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.CleanHistorialAdapter
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseHistorial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanHistorialFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var progress: ProgressBar

    private val api by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clean_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerHistorial)
        progress = view.findViewById(R.id.progressHistorial)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        cargarHistorial()
    }

    private fun cargarHistorial() {

        val prefs = requireActivity().getSharedPreferences(
            "ecotec_user",
            AppCompatActivity.MODE_PRIVATE
        )
        val area = prefs.getString("area", "") ?: ""

        // 🟣 Tabla de coincidencias
        val coincidencia = when (area) {
            "Cafetería" -> listOf("Cafetería - PB")
            "Edificio L (Planta Alta)" -> listOf("Edificio L - P1")
            "Edificio L (Planta Baja)" -> listOf("Edificio L - PB")
            "Edificio K (Planta Alta)" -> listOf("Edificio K - P1")
            "Edificio K (Planta Baja)" -> listOf("Edificio K - PB")
            "Edificio G (Planta Alta)" -> listOf("Edificio G - P1")
            "Edificio G (Planta Baja)" -> listOf("Edificio G - PB")
            "Edificio CC" -> listOf("Edificio CC", "Centro de Cómputo - PB")
            "Control general", "General" -> emptyList()
            else -> emptyList()
        }

        progress.visibility = View.VISIBLE

        api.getHistorialLimpieza().enqueue(object : Callback<ResponseHistorial> {
            override fun onResponse(
                call: Call<ResponseHistorial>,
                res: Response<ResponseHistorial>
            ) {

                progress.visibility = View.GONE

                val lista = res.body()?.historial ?: emptyList()

                // 🟡 Normalizar ubicaciones + evitar cualquier crash
                val normalizados = lista.map { item ->

                    val ubi = (item.ubicacion ?: "").lowercase()

                    val nuevaUbicacion = when {
                        ubi.contains("edificio l") && ubi.contains("p1") -> "Edificio L - P1"
                        ubi.contains("edificio l") && ubi.contains("pb") -> "Edificio L - PB"

                        ubi.contains("edificio g") && ubi.contains("p1") -> "Edificio G - P1"
                        ubi.contains("edificio g") && ubi.contains("pb") -> "Edificio G - PB"

                        ubi.contains("edificio k") && ubi.contains("p1") -> "Edificio K - P1"
                        ubi.contains("edificio k") && ubi.contains("pb") -> "Edificio K - PB"

                        ubi.contains("cafeter") -> "Cafetería - PB"

                        ubi.contains("centro") || ubi.contains("cómputo") -> "Centro de Cómputo - PB"

                        else -> item.ubicacion ?: "Sin ubicación"
                    }

                    // ⭐ copy SEGURO — evita todos los null crash
                    item.copy(
                        lectura_id = item.lectura_id ?: "",
                        bote_id = item.bote_id ?: "",
                        estado = item.estado ?: "",
                        nivel = item.nivel ?: 0,
                        tipo_residuo = item.tipo_residuo ?: "",
                        ubicacion = nuevaUbicacion,
                        fecha = item.fecha ?: ""
                    )
                }

                // 🔍 Filtrar por área asignada
                val filtrado = if (coincidencia.isEmpty()) {
                    normalizados
                } else {
                    normalizados.filter { h ->
                        coincidencia.any { match ->
                            (h.ubicacion ?: "").contains(match, ignoreCase = true)
                        }
                    }
                }

                recycler.adapter = CleanHistorialAdapter(filtrado)
            }

            override fun onFailure(call: Call<ResponseHistorial>, t: Throwable) {
                progress.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
