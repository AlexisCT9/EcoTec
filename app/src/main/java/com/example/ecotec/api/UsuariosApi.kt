package com.example.ecotec.api

import com.example.ecotec.models.Usuario
import okhttp3.*
import org.json.JSONObject

class UsuariosApi {

    private val client = OkHttpClient()
    private val BASE = "http://192.168.1.81/ecotec_api/usuarios/"

    // ============================================================
    // 🚀 OBTENER USUARIOS
    // ============================================================
    fun obtenerUsuarios(): List<Usuario>? {

        val req = Request.Builder()
            .url(BASE + "obtener_usuarios.php")
            .build()

        client.newCall(req).execute().use { resp ->

            val body = resp.body?.string() ?: return null
            val json = JSONObject(body)

            if (!json.getBoolean("success")) return null

            val arr = json.getJSONArray("usuarios")
            val lista = ArrayList<Usuario>()

            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)

                lista.add(
                    Usuario(
                        user_id = o.getString("user_id"),
                        nombre = o.getString("nombre"),
                        correo = o.getString("correo"),
                        telefono = o.getString("telefono"),
                        rol = o.getString("rol"),
                        area = o.getString("area"),
                        estado = o.getString("estado") // ← AGREGADO
                    )
                )
            }

            return lista
        }
    }

    // ============================================================
    // 🚀 AGREGAR USUARIO
    // ============================================================
    fun agregarUsuario(
        nombre: String,
        correo: String,
        telefono: String,
        rol: String,
        area: String,
        pass: String
    ): Boolean {

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("nombre", nombre)
            .addFormDataPart("correo", correo)
            .addFormDataPart("telefono", telefono)
            .addFormDataPart("rol", rol)
            .addFormDataPart("area", area)
            .addFormDataPart("password", pass)
            .build()

        val req = Request.Builder()
            .url(BASE + "agregar_usuario.php")
            .post(body)
            .build()

        client.newCall(req).execute().use {
            val json = it.body?.string() ?: return false
            return json.contains("success")
        }
    }

    // ============================================================
    // 🚀 ELIMINAR USUARIO
    // ============================================================
    fun eliminarUsuario(id: String): Boolean {

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", id)
            .build()

        val req = Request.Builder()
            .url(BASE + "eliminar_usuario.php")
            .post(body)
            .build()

        client.newCall(req).execute().use {
            val json = it.body?.string() ?: return false
            return json.contains("success")
        }
    }

    // ============================================================
    // 🚀 EDITAR USUARIO  (CON ESTADO)
    // ============================================================
    fun editarUsuario(
        id: String,
        nombre: String,
        correo: String,
        telefono: String,
        area: String,
        rol: String,
        estado: String
    ): Boolean {

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", id)
            .addFormDataPart("nombre", nombre)
            .addFormDataPart("correo", correo)
            .addFormDataPart("telefono", telefono)
            .addFormDataPart("area", area)
            .addFormDataPart("rol", rol)
            .addFormDataPart("estado", estado)   // ← AGREGADO
            .build()

        val req = Request.Builder()
            .url(BASE + "editar_usuario.php")
            .post(body)
            .build()

        client.newCall(req).execute().use {
            val json = it.body?.string() ?: return false
            return json.contains("success")
        }
    }
}
