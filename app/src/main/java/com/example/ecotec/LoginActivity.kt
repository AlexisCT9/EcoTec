package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var edtCorreo: EditText
    private lateinit var edtPassword: EditText
    private lateinit var txtEstado: TextView
    private lateinit var btnAdmin: LinearLayout
    private lateinit var btnClean: LinearLayout
    private lateinit var btnLogin: Button

    private var rolSeleccionado = "Administrador"

    private val client = OkHttpClient()
    private val URL = "http://192.168.1.81/ecotec_api/usuarios/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // REFERENCIAS CORRECTAS
        edtCorreo = findViewById(R.id.edtCorreo)
        edtPassword = findViewById(R.id.edtPassword)
        txtEstado = findViewById(R.id.txtEstado)
        btnAdmin = findViewById(R.id.btnAdmin)
        btnClean = findViewById(R.id.btnClean)
        btnLogin = findViewById(R.id.btnLogin)

        // ===============================================================
        // ⭐ OJITO Mostrar/Ocultar contraseña
        // ===============================================================
        val btnTogglePass = findViewById<ImageView>(R.id.btnTogglePass)
        var passVisible = false

        btnTogglePass.setOnClickListener {
            passVisible = !passVisible

            if (passVisible) {
                edtPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnTogglePass.setImageResource(R.drawable.ic_eye)
            } else {
                edtPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePass.setImageResource(R.drawable.ic_eye_off)
            }

            edtPassword.setSelection(edtPassword.text.length) // Mantiene el cursor al final
        }


        // EVENTOS
        btnAdmin.setOnClickListener { seleccionarRol("Administrador") }
        btnClean.setOnClickListener { seleccionarRol("Personal de Limpieza") }
        btnLogin.setOnClickListener { login() }

        // Rol inicial
        seleccionarRol("Administrador")
    }

    // ===============================================================
    // SELECCIÓN DE ROL
    // ===============================================================
    private fun seleccionarRol(rol: String) {

        rolSeleccionado = rol

        if (rol == "Administrador") {
            btnAdmin.setBackgroundResource(R.drawable.bg_role_selected)
            btnClean.setBackgroundResource(R.drawable.bg_role_unselected)
        } else {
            btnAdmin.setBackgroundResource(R.drawable.bg_role_unselected)
            btnClean.setBackgroundResource(R.drawable.bg_role_selected_green)
        }
    }

    // ===============================================================
    // LOGIN
    // ===============================================================
    private fun login() {
        val correo = edtCorreo.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        txtEstado.visibility = View.GONE

        if (correo.isEmpty() || password.isEmpty()) {
            mostrarEstado("Completa todos los campos", false)
            return
        }

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("correo", correo)
            .addFormDataPart("password", password)
            .addFormDataPart("rol", rolSeleccionado)
            .build()

        val req = Request.Builder().url(URL).post(body).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { mostrarEstado("Error de conexión", false) }
            }

            override fun onResponse(call: Call, response: Response) {
                val resp = response.body?.string() ?: ""

                runOnUiThread {
                    when {
                        resp.contains("success") -> {
                            mostrarEstado("¡Inicio de sesión exitoso!", true)

                            // ===============================================================
                            // ⭐ REDIRECCIÓN
                            // ===============================================================
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                if (rolSeleccionado == "Administrador") {
                                    startActivity(Intent(this@LoginActivity, AdminDashboardActivity::class.java))
                                } else {
                                    startActivity(Intent(this@LoginActivity, CleanDashboardActivity::class.java))
                                }
                            }, 1000)
                        }

                        resp.contains("error_credentials") ->
                            mostrarEstado("Correo o contraseña incorrectos", false)

                        resp.contains("error_role") ->
                            mostrarEstado("Rol incorrecto para este usuario", false)

                        resp.contains("user_suspended") ->
                            mostrarEstado("Usuario suspendido", false)

                        else ->
                            mostrarEstado("Error inesperado", false)
                    }
                }
            }
        })
    }

    // ===============================================================
    // ALERTA VISUAL (VERDE / ROJO)
    // ===============================================================
    private fun mostrarEstado(msg: String, success: Boolean) {
        txtEstado.visibility = View.VISIBLE
        txtEstado.text = msg

        txtEstado.setBackgroundResource(
            if (success) R.drawable.alert_success
            else R.drawable.alert_error
        )

        txtEstado.setTextColor(
            if (success) 0xFF0A7C39.toInt()
            else 0xFFB00020.toInt()
        )
    }
}
