package com.example.ecotec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class CleanHomeFragment : Fragment() {

    private var area: String = ""

    companion object {
        fun newInstance(area: String): CleanHomeFragment {
            val f = CleanHomeFragment()
            f.area = area
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clean_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val prefs = requireActivity().getSharedPreferences("ecotec_user",
            AppCompatActivity.MODE_PRIVATE)

        val nombre = prefs.getString("nombre", "Usuario de Limpieza")
        val area = prefs.getString("area", area)

        val txtArea = view.findViewById<TextView>(R.id.txtAreaAsignada)
        val txtResumen = view.findViewById<TextView>(R.id.txtResumen)
        val txtBasureros = view.findViewById<TextView>(R.id.txtBasureros)

        txtArea.text = "Área asignada: $area"
        txtResumen.text = "Hola $nombre 👋"
        txtBasureros.text = "Basureros asignados: 5"
    }
}
