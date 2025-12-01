package com.example.ecotec

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.api.UsuariosApi
import com.example.ecotec.adapters.UsuariosAdapter
import kotlin.concurrent.thread

class UsuariosFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = RecyclerView(requireContext())
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val container = view.findViewById<LinearLayout>(R.id.containerUsuarios)
        container.addView(recycler)

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        thread {
            val api = UsuariosApi()
            val resp = api.obtenerUsuarios()

            activity?.runOnUiThread {
                if (resp != null && resp.success) {
                    recycler.adapter = UsuariosAdapter(
                        resp.data,
                        onEditar = { usuario -> editarUsuario(usuario) },
                        onEliminar = { usuario -> eliminarUsuario(usuario) }
                    )
                } else {
                    Toast.makeText(requireContext(), "Error al conectar API", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editarUsuario(usuario: com.example.ecotec.models.Usuario) {
        Toast.makeText(requireContext(), "Editar: ${usuario.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun eliminarUsuario(usuario: com.example.ecotec.models.Usuario) {
        Toast.makeText(requireContext(), "Eliminar: ${usuario.nombre}", Toast.LENGTH_SHORT).show()
    }
}
