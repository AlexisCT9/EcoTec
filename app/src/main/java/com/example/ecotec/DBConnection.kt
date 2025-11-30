package com.example.ecotec

import android.util.Log
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

object DBConnection {

    private const val BASE_URL = "http://192.168.1.81/ecotec_api/"
    private val client = OkHttpClient()

    fun enviarJSON(ruta: String, jsonData: String, callback: (Boolean, String) -> Unit) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonData.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(BASE_URL + ruta)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DBConnection", "❌ Error: ${e.message}")
                callback(false, "No se pudo conectar con el servidor.")
            }

            override fun onResponse(call: Call, response: Response) {
                val respuesta = response.body?.string() ?: ""

                Log.e("DBConnection", "Respuesta PHP: $respuesta")

                if (response.isSuccessful) {
                    callback(true, respuesta)
                } else {
                    callback(false, respuesta)
                }
            }
        })
    }
}
