package com.example.ecotec

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseDetalleBasurero
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleBasureroActivity : AppCompatActivity() {

    private lateinit var txtId: TextView
    private lateinit var txtTipo: TextView
    private lateinit var txtUbicacion: TextView
    private lateinit var txtEstado: TextView
    private lateinit var barNivel: ProgressBar

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_basurero)

        txtId = findViewById(R.id.txtId)
        txtTipo = findViewById(R.id.txtTipo)
        txtUbicacion = findViewById(R.id.txtUbicacion)
        txtEstado = findViewById(R.id.txtEstado)
        barNivel = findViewById(R.id.progressNivel)

        val id = intent.getStringExtra("id") ?: return

        cargarDetalle(id)
    }

    private fun cargarDetalle(id: String) {
        api.getBasureroDetalle(id).enqueue(object : Callback<ResponseDetalleBasurero> {
            override fun onResponse(
                call: Call<ResponseDetalleBasurero>,
                response: Response<ResponseDetalleBasurero>
            ) {
                val b = response.body()?.basurero ?: return

                txtId.text = b.bote_id
                txtTipo.text = b.tipo_residuo
                txtUbicacion.text = b.ubicacion
                txtEstado.text = b.estado
                barNivel.progress = b.nivel
            }

            override fun onFailure(call: Call<ResponseDetalleBasurero>, t: Throwable) {}
        })
    }
}
