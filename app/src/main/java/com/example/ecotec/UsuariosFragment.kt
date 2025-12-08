package com.example.ecotec

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.api.UsuariosApi
import com.example.ecotec.adapters.UsuariosAdapter
import com.example.ecotec.models.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class UsuariosFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var btnAgregarUsuario: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler = view.findViewById(R.id.recyclerUsuarios)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        btnAgregarUsuario = view.findViewById(R.id.btnAgregarUsuario)
        btnAgregarUsuario.setOnClickListener {
            mostrarDialogAgregar()
        }

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        thread {
            val api = UsuariosApi()
            val lista = api.obtenerUsuarios()

            activity?.runOnUiThread {
                if (lista != null) {
                    recycler.adapter = UsuariosAdapter(
                        lista,
                        onEditar = { usuario -> editarUsuario(usuario) },
                        onEliminar = { usuario -> eliminarUsuario(usuario) }
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al conectar con la API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // -----------------------------------------------------------
    // AGREGAR USUARIO
    // -----------------------------------------------------------
    private fun mostrarDialogAgregar() {

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_usuario, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombreNuevo)
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreoNuevo)
        val edtTelefono = dialogView.findViewById<EditText>(R.id.edtTelefonoNuevo)
        val spinnerRol = dialogView.findViewById<Spinner>(R.id.spinnerRol)
        val spinnerArea = dialogView.findViewById<Spinner>(R.id.spinnerArea)
        val edtPass = dialogView.findViewById<EditText>(R.id.edtPassNuevo)
        val btnVerPass = dialogView.findViewById<ImageView>(R.id.btnVerPass)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardarNuevo)

        // LISTA DE ROLES
        val roles = listOf("Administrador", "Personal de Limpieza", "Usuario General")
        spinnerRol.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        // LISTA DE ÁREAS
        val areas = listOf(
            "Cafetería",
            "Edificio L (Planta Alta)",
            "Edificio L (Planta Baja)",
            "Edificio K (Planta Alta)",
            "Edificio K (Planta Baja)",
            "Edificio G (Planta Alta)",
            "Edificio G (Planta Baja)",
            "Edificio CC"
        )
        spinnerArea.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            areas
        )

        // Mostrar / ocultar contraseña
        var visible = false
        btnVerPass.setOnClickListener {
            visible = !visible

            if (visible) {
                edtPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnVerPass.setImageResource(R.drawable.ic_eye)
            } else {
                edtPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnVerPass.setImageResource(R.drawable.ic_eye_off)
            }

            edtPass.setSelection(edtPass.text.length)
        }

        // GUARDAR
        btnGuardar.setOnClickListener {

            val nombre = edtNombre.text.toString()
            val correo = edtCorreo.text.toString()
            val telefono = edtTelefono.text.toString()
            val rol = spinnerRol.selectedItem.toString()
            val area = spinnerArea.selectedItem.toString()
            val pass = edtPass.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            thread {
                val api = UsuariosApi()
                val ok = api.agregarUsuario(nombre, correo, telefono, rol, area, pass)

                activity?.runOnUiThread {
                    if (ok) {
                        Toast.makeText(requireContext(), "Usuario agregado", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                        cargarUsuarios()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al agregar usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        dialog.show()
    }

    // -----------------------------------------------------------
    // EDITAR
    // -----------------------------------------------------------
    private fun editarUsuario(usuario: Usuario) {

        thread {
            val api = UsuariosApi()
            val ok = api.editarUsuario(
                usuario.user_id,
                usuario.nombre,
                usuario.correo,
                usuario.telefono,
                usuario.area,
                usuario.rol,
                usuario.estado   // ← CORREGIDO, AHORA SÍ SE ENVÍA
            )

            activity?.runOnUiThread {
                if (ok) {
                    Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT)
                        .show()
                    cargarUsuarios()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // -----------------------------------------------------------
    // ELIMINAR
    // -----------------------------------------------------------
    private fun eliminarUsuario(usuario: Usuario) {

        thread {
            val api = UsuariosApi()
            val ok = api.eliminarUsuario(usuario.user_id)

            activity?.runOnUiThread {
                if (ok) {
                    Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    cargarUsuarios()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al eliminar usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
