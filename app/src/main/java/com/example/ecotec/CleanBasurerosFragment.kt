package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.AdapterBasureros
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseBasureros
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanBasurerosFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clean_basureros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler = view.findViewById(R.id.recyclerBasurerosClean)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargar()
    }

    private fun cargar() {

        val prefs = requireActivity().getSharedPreferences("ecotec_user",
            AppCompatActivity.MODE_PRIVATE)

        val area = prefs.getString("area", "") ?: ""

        // MAPEADOR OFICIAL
        val coincidencia = when(area) {

            "Cafetería" -> listOf("Cafetería - PB")

            "Edificio L (Planta Alta)" -> listOf("Edificio L - P1")
            "Edificio L (Planta Baja)" -> listOf("Edificio L - PB")

            "Edificio K (Planta Alta)" -> listOf("Edificio K - P1")
            "Edificio K (Planta Baja)" -> listOf("Edificio K - PB")

            "Edificio G (Planta Alta)" -> listOf("Edificio G - P1")
            "Edificio G (Planta Baja)" -> listOf("Edificio G - PB")

            "Edificio CC" -> listOf("Edificio CC", "Centro de Cómputo - PB")

            "Control general", "General" -> emptyList() // ver todos

            else -> emptyList()
        }

        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {

            override fun onResponse(
                call: Call<ResponseBasureros>,
                response: Response<ResponseBasureros>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val lista = response.body()?.basureros ?: emptyList()

                val filtrados = if (coincidencia.isEmpty()) {
                    lista
                } else {
                    lista.filter { b ->
                        coincidencia.any { match -> b.ubicacion.contains(match, ignoreCase = true) }
                    }
                }

                recycler.adapter = AdapterBasureros(
                    filtrados,
                    onClick = { b ->
                        val intent = Intent(requireContext(), CleanDetalleBasureroActivity::class.java)
                        intent.putExtra("id", b.bote_id)
                        startActivity(intent)
                    }
                )
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
