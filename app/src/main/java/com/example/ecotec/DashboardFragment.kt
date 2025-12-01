package com.example.ecotec

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.Basurero
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private lateinit var txtUsuarios: TextView
    private lateinit var txtBasureros: TextView
    private lateinit var txtPendientes: TextView
    private lateinit var txtAtencion: TextView
    private lateinit var container: LinearLayout

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtUsuarios = view.findViewById(R.id.txtUsuarios)
        txtBasureros = view.findViewById(R.id.txtBasureros)
        txtPendientes = view.findViewById(R.id.txtPendientes)
        txtAtencion = view.findViewById(R.id.txtAtencion)
        container = view.findViewById(R.id.containerBasureros)

        loadDashboard()
    }

    private fun loadDashboard() {
        loadTotalUsuarios()
        loadTotalBasureros()
        loadEstadisticas()
        loadBasurerosEstado()
    }

    private fun loadTotalUsuarios() {
        api.getTotalUsuarios().enqueue(object : Callback<com.example.ecotec.models.ResponseTotal> {
            override fun onResponse(
                call: Call<com.example.ecotec.models.ResponseTotal>,
                response: Response<com.example.ecotec.models.ResponseTotal>
            ) {
                txtUsuarios.text = response.body()?.total.toString()
            }

            override fun onFailure(call: Call<com.example.ecotec.models.ResponseTotal>, t: Throwable) {}
        })
    }

    private fun loadTotalBasureros() {
        api.getTotalBasureros().enqueue(object : Callback<com.example.ecotec.models.ResponseTotal> {
            override fun onResponse(
                call: Call<com.example.ecotec.models.ResponseTotal>,
                response: Response<com.example.ecotec.models.ResponseTotal>
            ) {
                txtBasureros.text = response.body()?.total.toString()
            }

            override fun onFailure(call: Call<com.example.ecotec.models.ResponseTotal>, t: Throwable) {}
        })
    }

    private fun loadEstadisticas() {
        api.getEstadisticasReportes().enqueue(object : Callback<com.example.ecotec.models.ResponseReportes> {
            override fun onResponse(
                call: Call<com.example.ecotec.models.ResponseReportes>,
                response: Response<com.example.ecotec.models.ResponseReportes>
            ) {
                response.body()?.let {
                    txtPendientes.text = it.pendientes.toString()
                    txtAtencion.text = it.atendidos.toString()
                }
            }

            override fun onFailure(call: Call<com.example.ecotec.models.ResponseReportes>, t: Throwable) {}
        })
    }

    private fun loadBasurerosEstado() {
        api.getBasurerosEstado().enqueue(object : Callback<com.example.ecotec.models.ResponseBasureros> {
            override fun onResponse(
                call: Call<com.example.ecotec.models.ResponseBasureros>,
                response: Response<com.example.ecotec.models.ResponseBasureros>
            ) {
                container.removeAllViews()
                response.body()?.basureros?.forEach {
                    addBasurero(it)
                }
            }

            override fun onFailure(call: Call<com.example.ecotec.models.ResponseBasureros>, t: Throwable) {}
        })
    }

    private fun addBasurero(b: Basurero) {

        val item = LinearLayout(requireContext())
        item.orientation = LinearLayout.VERTICAL
        item.setPadding(20, 20, 20, 20)
        item.setBackgroundResource(R.drawable.bg_card_white)

        val title = TextView(requireContext())
        title.text = b.id
        title.textSize = 18f
        title.setTextColor(Color.BLACK)

        val ub = TextView(requireContext())
        ub.text = b.ubicacion
        ub.setTextColor(Color.DKGRAY)

        val bar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal)
        bar.progress = b.nivel
        bar.max = 100

        when (b.estado) {
            "rojo" -> bar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_red)
            "amarillo" -> bar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_yellow)
            else -> bar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_green)
        }

        container.addView(item)
        item.addView(title)
        item.addView(ub)
        item.addView(bar)
    }
}
