package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CleanPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_clean_perfil, container, false)

        // Referencias a vistas
        val txtNombre = view.findViewById<TextView>(R.id.txtNombrePerfil)
        val txtArea = view.findViewById<TextView>(R.id.txtAreaPerfil)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // Recuperar datos del usuario (que llegan por Dashboard)
        val areaAsignada = activity?.intent?.getStringExtra("area") ?: "Sin área"

        txtNombre.text = "Usuario de Limpieza"
        txtArea.text = "Área asignada: $areaAsignada"

        // Cerrar sesión
        btnLogout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
