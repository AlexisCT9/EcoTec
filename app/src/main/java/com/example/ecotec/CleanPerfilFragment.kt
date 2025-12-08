package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class CleanPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_clean_perfil, container, false)

        val prefs = requireActivity().getSharedPreferences("ecotec_user",
            AppCompatActivity.MODE_PRIVATE)

        val nombre = prefs.getString("nombre", "Usuario de Limpieza")
        val area = prefs.getString("area", "Sin área")
        val correo = prefs.getString("correo", "email@ejemplo.com")
        val telefono = prefs.getString("telefono", "000-000-0000")

        val txtNombre = view.findViewById<TextView>(R.id.txtNombrePerfil)
        val txtArea = view.findViewById<TextView>(R.id.txtAreaPerfil)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreoPerfil)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefonoPerfil)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        txtNombre.text = nombre
        txtArea.text = "Área asignada: $area"
        txtCorreo.text = correo
        txtTelefono.text = telefono

        btnLogout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
