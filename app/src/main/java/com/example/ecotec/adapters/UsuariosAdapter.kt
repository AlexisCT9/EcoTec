package com.example.ecotec.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.R
import com.example.ecotec.models.Usuario

class UsuariosAdapter(
    private var lista: List<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onEliminar: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarText: TextView = view.findViewById(R.id.avatarText)

        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreo)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)

        val txtRolTag: TextView = view.findViewById(R.id.txtRolTag)
        val txtAreaTag: TextView = view.findViewById(R.id.txtAreaTag)

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

        // Avatar con iniciales
        holder.avatarText.text = obtenerIniciales(u.nombre)

        // Datos
        holder.txtNombre.text = u.nombre
        holder.txtCorreo.text = u.correo
        holder.txtTelefono.text = u.telefono

        holder.txtRolTag.text = u.rol
        holder.txtAreaTag.text = u.area

        // Eventos
        holder.btnEditar.setOnClickListener {
            mostrarDialogEditar(holder.itemView, u)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(u)
        }
    }

    override fun getItemCount(): Int = lista.size


    // =======================================
    //   FUNCION PARA OBTENER INICIALES
    // =======================================
    private fun obtenerIniciales(nombre: String): String {
        val partes = nombre.trim().split(" ")
        return when {
            partes.size >= 2 -> partes[0].take(1) + partes[1].take(1)
            partes.isNotEmpty() -> partes[0].take(1)
            else -> "?"
        }.uppercase()
    }


    // =======================================
    //   DIALOG PARA EDITAR
    // =======================================
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
        val spinnerRol = dialogView.findViewById<Spinner>(R.id.spinnerRolEdit)
        val spinnerArea = dialogView.findViewById<Spinner>(R.id.spinnerAreaEdit)
        val edtPass = dialogView.findViewById<EditText>(R.id.edtPassEdit)
        val btnVerPass = dialogView.findViewById<ImageView>(R.id.btnVerPassEdit)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardarEdit)

        // Asignar datos actuales
        edtNombre.setText(usuario.nombre)
        edtCorreo.setText(usuario.correo)
        edtTelefono.setText(usuario.telefono)

        // Spinner Rol
        val roles = arrayOf("Administrador", "Personal de Limpieza", "Usuario General")
        spinnerRol.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, roles)

        spinnerRol.setSelection(roles.indexOf(usuario.rol))

        // Spinner Área
        val areas = arrayOf(
            "Cafetería",
            "Edificio L (Planta Alta)",
            "Edificio L (Planta Baja)",
            "Edificio K (Planta Alta)",
            "Edificio K (Planta Baja)",
            "Edificio G (Planta Alta)",
            "Edificio G (Planta Baja)",
            "Edificio CC"
        )

        spinnerArea.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, areas)
        spinnerArea.setSelection(areas.indexOf(usuario.area))

        // Mostrar / ocultar contraseña
        btnVerPass.setOnClickListener {
            if (edtPass.inputType == 129) { // PASSWORD
                edtPass.inputType = 1
                btnVerPass.setImageResource(R.drawable.ic_eye)
            } else {
                edtPass.inputType = 129
                btnVerPass.setImageResource(R.drawable.ic_eye_off)
            }
        }

        // Guardar cambios
        btnGuardar.setOnClickListener {

            val actualizado = Usuario(
                id = usuario.id,
                nombre = edtNombre.text.toString(),
                correo = edtCorreo.text.toString(),
                telefono = edtTelefono.text.toString(),
                rol = spinnerRol.selectedItem.toString(),
                area = spinnerArea.selectedItem.toString()
            )

            onEditar(actualizado)
            dialog.dismiss()
        }

        dialog.show()
    }
}
