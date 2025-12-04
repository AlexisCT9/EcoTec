package com.example.ecotec

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseDetalleBasurero
import com.example.ecotec.models.ResponseSimple
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanDetalleBasureroActivity : AppCompatActivity() {

    private lateinit var txtId: TextView
    private lateinit var txtUbicacion: TextView
    private lateinit var txtTipo: TextView
    private lateinit var txtNivel: TextView
    private lateinit var txtEstado: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnAtender: Button

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    private var basureroId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_detalle)

        txtId = findViewById(R.id.cleanDetId)
        txtUbicacion = findViewById(R.id.cleanDetUbicacion)
        txtTipo = findViewById(R.id.cleanDetTipo)
        txtNivel = findViewById(R.id.cleanDetNivel)
        txtEstado = findViewById(R.id.cleanDetEstado)
        progress = findViewById(R.id.cleanDetProgress)
        btnAtender = findViewById(R.id.btnMarcarAtendido)

        basureroId = intent.getStringExtra("id") ?: ""

        if (basureroId.isEmpty()) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        obtenerDetalle()

        btnAtender.setOnClickListener {
            marcarAtendido()
        }
    }

    private fun obtenerDetalle() {
        progress.visibility = View.VISIBLE

        api.getBasureroDetalle(basureroId).enqueue(object : Callback<ResponseDetalleBasurero> {
            override fun onResponse(
                call: Call<ResponseDetalleBasurero>,
                response: Response<ResponseDetalleBasurero>
            ) {
                progress.visibility = View.GONE

                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(this@CleanDetalleBasureroActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    return
                }

                val b = response.body()!!.basurero

                txtId.text = b.bote_id
                txtUbicacion.text = b.ubicacion
                txtTipo.text = b.tipo_residuo
                txtNivel.text = "${b.nivel}%"
                txtEstado.text = b.estado

            }

            override fun onFailure(call: Call<ResponseDetalleBasurero>, t: Throwable) {
                progress.visibility = View.GONE
                Toast.makeText(this@CleanDetalleBasureroActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ======================================================
    // MARCAR COMO ATENDIDO (nivel=0, estado="vacío")
    // ======================================================
    private fun marcarAtendido() {
        progress.visibility = View.VISIBLE

        api.marcarAtendido(basureroId)
            .enqueue(object : Callback<ResponseSimple> {
                override fun onResponse(
                    call: Call<ResponseSimple>,
                    response: Response<ResponseSimple>
                ) {
                    progress.visibility = View.GONE

                    if (!response.isSuccessful) {
                        Toast.makeText(this@CleanDetalleBasureroActivity, "Error del servidor", Toast.LENGTH_SHORT).show()
                        return
                    }

                    Toast.makeText(this@CleanDetalleBasureroActivity, "Basurero marcado como atendido", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onFailure(call: Call<ResponseSimple>, t: Throwable) {
                    progress.visibility = View.GONE
                    Toast.makeText(this@CleanDetalleBasureroActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


}
