package com.example.ecotec.api

import com.example.ecotec.models.*
import okhttp3.*
import com.google.gson.Gson

class UsuariosApi {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val BASE = "http://192.168.1.81/ecotec_api/usuarios/"

    fun obtenerUsuarios(): ResponseUsuarios? {
        val req = Request.Builder().url(BASE + "obtener_usuarios.php").build()

        client.newCall(req).execute().use {
            val body = it.body?.string() ?: return null
            return gson.fromJson(body, ResponseUsuarios::class.java)
        }
    }

    fun eliminarUsuario(id: Int): Boolean {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", id.toString())
            .build()

        val req = Request.Builder().url(BASE + "eliminar_usuario.php").post(body).build()

        client.newCall(req).execute().use {
            val json = it.body?.string() ?: return false
            return json.contains("success")
        }
    }

    fun editarUsuario(id: Int, nombre: String, correo: String, rol: String): Boolean {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", id.toString())
            .addFormDataPart("nombre", nombre)
            .addFormDataPart("correo", correo)
            .addFormDataPart("rol", rol)
            .build()

        val req = Request.Builder().url(BASE + "editar_usuario.php").post(body).build()

        client.newCall(req).execute().use {
            val json = it.body?.string() ?: return false
            return json.contains("success")
        }
    }
}
