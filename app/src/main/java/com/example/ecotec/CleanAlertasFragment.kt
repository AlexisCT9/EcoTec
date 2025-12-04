package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.AlertasAdapter
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseAlertas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanAlertasFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clean_alertas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargarAlertas()
    }

    private fun cargarAlertas() {
        api.getAlertasPendientes().enqueue(object : Callback<ResponseAlertas> {
            override fun onResponse(call: Call<ResponseAlertas>, response: Response<ResponseAlertas>) {
                if (!response.isSuccessful) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val lista = response.body()?.alertas ?: emptyList()

                recycler.adapter = AlertasAdapter(lista) { alerta ->
                    val intent = Intent(requireContext(), CleanDetalleAlertaActivity::class.java)
                    intent.putExtra("notif_id", alerta.notif_id)
                    intent.putExtra("mensaje", alerta.mensaje)
                    intent.putExtra("fecha", alerta.fecha)
                    intent.putExtra("tipo", alerta.tipo_notificacion)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseAlertas>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
