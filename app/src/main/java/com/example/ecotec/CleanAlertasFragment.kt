package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.AlertasAdapter
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseAlertas
import com.example.ecotec.models.ResponseBasureros
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanAlertasFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val api by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clean_alertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        cargarAlertas()
    }

    // ============================================================
    // 💛 FUNCIÓN DE COINCIDENCIA INTELIGENTE (OPCIÓN 2)
    // ============================================================
    private fun coincide(area: String, ubicacion: String): Boolean {

        val a = area.lowercase()
        val u = ubicacion.lowercase()

        // 1. Planta Alta = P1
        if (a.contains("planta alta") && u.contains("p1")) return true

        // 2. Planta Baja = PB
        if (a.contains("planta baja") && u.contains("pb")) return true

        // 3. Cafetería = CC
        if (a.contains("cafetería") && u.contains("cc")) return true

        // 4. Centro de Cómputo = CC
        if (a.contains("centro de cómputo") && u.contains("cc")) return true

        // 5. Edificios L/G/K – coincidencia flexible
        if (a.contains("edificio l") && u.contains("edificio l")) return true
        if (a.contains("edificio g") && u.contains("edificio g")) return true
        if (a.contains("edificio k") && u.contains("edificio k")) return true

        // 6. Último recurso: coincidencia directa
        return u.contains(a, ignoreCase = true)
    }

    // ============================================================
    // 💛 CARGAR ALERTAS
    // ============================================================
    private fun cargarAlertas() {

        val prefs = requireActivity().getSharedPreferences("ecotec_user",
            AppCompatActivity.MODE_PRIVATE)
        val area = prefs.getString("area", "") ?: ""

        // 1. Obtener basureros
        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {
            override fun onResponse(call: Call<ResponseBasureros>, basRes: Response<ResponseBasureros>) {

                val basureros = basRes.body()?.basureros ?: emptyList()

                // 2. Obtener alertas
                api.getAlertasPendientes().enqueue(object : Callback<ResponseAlertas> {
                    override fun onResponse(call: Call<ResponseAlertas>, alertRes: Response<ResponseAlertas>) {

                        val alertas = alertRes.body()?.alertas ?: emptyList()

                        // 3. Filtrado inteligente
                        val filtradas = alertas.filter { alerta ->
                            val b = basureros.find { it.bote_id == alerta.bote_id }
                            b != null && coincide(area, b.ubicacion)
                        }

                        recycler.adapter = AlertasAdapter(filtradas) { alerta ->
                            val i = Intent(requireContext(), CleanDetalleAlertaActivity::class.java)
                            i.putExtra("notif_id", alerta.notif_id)
                            i.putExtra("mensaje", alerta.mensaje)
                            i.putExtra("fecha", alerta.fecha)
                            i.putExtra("tipo", alerta.tipo_notificacion)
                            startActivity(i)
                        }
                    }

                    override fun onFailure(call: Call<ResponseAlertas>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
