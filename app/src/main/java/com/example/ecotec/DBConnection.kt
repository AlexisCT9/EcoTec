package com.example.ecotec

import android.net.Uri
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

object DBConnection {

    private const val BASE_URL = "http://192.168.1.81/ecotec_api/"
    private val client = OkHttpClient()

    fun enviarReporteConFoto(
        ruta: String,
        parametros: Map<String, String>,
        fotoFile: File?,
        callback: (Boolean, String) -> Unit
    ) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        // Agregar parámetros normales
        for ((clave, valor) in parametros) {
            builder.addFormDataPart(clave, valor)
        }

        // Agregar foto
        if (fotoFile != null && fotoFile.exists()) {
            builder.addFormDataPart(
                "foto",
                fotoFile.name,
                fotoFile.asRequestBody("image/jpeg".toMediaType())
            )
        }

        val requestBody = builder.build()

        val request = Request.Builder()
            .url(BASE_URL + ruta)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "Error de conexión: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string() ?: ""
                if (response.isSuccessful) callback(true, resp)
                else callback(false, resp)
            }
        })
    }
}
