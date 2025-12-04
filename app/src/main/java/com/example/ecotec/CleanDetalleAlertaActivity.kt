package com.example.ecotec

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanDetalleAlertaActivity : AppCompatActivity() {

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_detalle_alerta)

        val txtTipo = findViewById<TextView>(R.id.txtTipo)
        val txtMensaje = findViewById<TextView>(R.id.txtMensaje)
        val txtFecha = findViewById<TextView>(R.id.txtFecha)
        val btnResolver = findViewById<Button>(R.id.btnResolver)

        val notifId = intent.getStringExtra("notif_id") ?: return

        txtTipo.text = intent.getStringExtra("tipo")
        txtMensaje.text = intent.getStringExtra("mensaje")
        txtFecha.text = intent.getStringExtra("fecha")

        btnResolver.setOnClickListener {
            api.resolverAlerta(notifId).enqueue(object : Callback<com.example.ecotec.models.ResponseSimple> {
                override fun onResponse(
                    call: Call<com.example.ecotec.models.ResponseSimple>,
                    response: Response<com.example.ecotec.models.ResponseSimple>
                ) {
                    Toast.makeText(this@CleanDetalleAlertaActivity, "Alerta resuelta", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onFailure(call: Call<com.example.ecotec.models.ResponseSimple>, t: Throwable) {
                    Toast.makeText(this@CleanDetalleAlertaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
