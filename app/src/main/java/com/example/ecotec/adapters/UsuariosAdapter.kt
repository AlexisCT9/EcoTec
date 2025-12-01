package com.example.ecotec.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.Usuario

class UsuariosAdapter(
    private var lista: List<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onEliminar: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreo)
        val txtRol: TextView = view.findViewById(R.id.txtRol)
        val btnEditar: TextView = view.findViewById(R.id.btnEditar)
        val btnEliminar: TextView = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val u = lista[position]

        holder.txtNombre.text = u.nombre
        holder.txtCorreo.text = u.correo
        holder.txtRol.text = u.rol

        // EDITAR
        holder.btnEditar.setOnClickListener {
            mostrarDialogEditar(holder.itemView, u)
        }

        // ELIMINAR
        holder.btnEliminar.setOnClickListener {
            onEliminar(u)
        }
    }

    override fun getItemCount(): Int = lista.size

    // ===============================================================
    // POPUP EDITAR
    // ===============================================================
    private fun mostrarDialogEditar(view: View, usuario: Usuario) {
        val context = view.context
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_editar_usuario, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val edtNombre = dialogView.findViewById<EditText>(R.id.edtNombreEdit)
        val edtCorreo = dialogView.findViewById<EditText>(R.id.edtCorreoEdit)
        val edtTelefono = dialogView.findViewById<EditText>(R.id.edtTelefonoEdit)
        val edtRol = dialogView.findViewById<EditText>(R.id.edtRolEdit)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardarEdit)

        // Datos actuales
        edtNombre.setText(usuario.nombre)
        edtCorreo.setText(usuario.correo)
        edtTelefono.setText(usuario.telefono)
        edtRol.setText(usuario.rol)

        btnGuardar.setOnClickListener {

            val actualizado = Usuario(
                id = usuario.id,
                nombre = edtNombre.text.toString(),
                correo = edtCorreo.text.toString(),
                telefono = edtTelefono.text.toString(),
                rol = edtRol.text.toString()
            )

            onEditar(actualizado)
            dialog.dismiss()
        }

        dialog.show()
    }
}
