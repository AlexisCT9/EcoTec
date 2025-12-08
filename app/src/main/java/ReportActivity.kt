package com.example.ecotec

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.Basurero
import com.example.ecotec.models.ResponseBasureros
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ReportActivity : AppCompatActivity() {

    private var fotoUri: Uri? = null
    private val client = OkHttpClient()

    private lateinit var fotoPreview: ImageView
    private lateinit var iconCamera: ImageView
    private lateinit var textFoto: TextView
    private lateinit var layoutFoto: LinearLayout

    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerContenedor: Spinner
    private lateinit var edtDescripcion: EditText

    private val basureros = mutableListOf<Basurero>()
    private val basurerosLabels = mutableListOf<String>()
    private val basurerosIds = mutableListOf<String>()

    // Retrofit
    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    // ------------------------------
    // PERMISOS CAMERA/GALERÍA
    // ------------------------------
    private val permisoCamaraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permitido ->
            if (permitido) abrirSelectorDeFoto()
            else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }

    private val permisoGaleriaLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permitido ->
            if (permitido) abrirGaleria()
            else Toast.makeText(this, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        fotoPreview = findViewById(R.id.fotoPreview)
        iconCamera = findViewById(R.id.iconCamera)
        textFoto = findViewById(R.id.textFoto)
        layoutFoto = findViewById(R.id.layoutFoto)

        spinnerTipo = findViewById(R.id.spinnerTipo)
        spinnerContenedor = findViewById(R.id.spinnerContenedor)
        edtDescripcion = findViewById(R.id.edtDescripcion)

        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        // ------------------------------
        // MENU
        // ------------------------------
        menuIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_map -> { startActivity(Intent(this, MapActivity::class.java)); true }
                    R.id.action_recommend -> { startActivity(Intent(this, RecommendActivity::class.java)); true }
                    R.id.action_guide -> { startActivity(Intent(this, GuideActivity::class.java)); true }
                    R.id.action_login -> { startActivity(Intent(this, LoginActivity::class.java)); true }
                    R.id.action_report -> {
                        Toast.makeText(this, "Ya estás en Reportar ⚠️", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        // ------------------------------
        // SPINNER TIPO
        // ------------------------------
        spinnerTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf(
                "Seleccionar tipo",
                "Tapa atascada o rota",
                "Sensor no funciona",
                "Contenedor desbordado",
                "Cámara no identifica residuos",
                "Otro problema"
            )
        )

        // ------------------------------
        // CARGAR CONTENEDORES DESDE LA API
        // ------------------------------
        cargarBasureros()

        layoutFoto.setOnClickListener { pedirPermisoCamara() }

        btnEnviar.setOnClickListener { enviarReporte() }
    }

    // ================================================================
    // Cargar basureros reales desde el servidor
    // ================================================================
    private fun cargarBasureros() {
        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {

            override fun onResponse(
                call: Call<ResponseBasureros>,
                response: Response<ResponseBasureros>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@ReportActivity, "Error servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val lista = response.body()?.basureros ?: emptyList()

                basureros.clear()
                basureros.addAll(lista)

                // Label y ID reales
                basurerosLabels.clear()
                basurerosIds.clear()

                basurerosLabels.add("Seleccionar contenedor")
                basurerosIds.add("")

                lista.forEach { b ->
                    basurerosLabels.add("${b.bote_id} - ${b.ubicacion}")
                    basurerosIds.add(b.bote_id)
                }

                spinnerContenedor.adapter =
                    ArrayAdapter(
                        this@ReportActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        basurerosLabels
                    )
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                Toast.makeText(this@ReportActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ================================================================
    // PERMISOS Y FOTO
    // ================================================================
    private fun pedirPermisoCamara() {
        permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun abrirSelectorDeFoto() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar imagen")
            .setItems(opciones) { _, i ->
                when (i) {
                    0 -> abrirCamara()
                    1 -> pedirPermisoGaleria()
                }
            }.show()
    }

    private fun pedirPermisoGaleria() {
        permisoGaleriaLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
    }

    private val camaraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = it.data?.extras?.get("data") as? android.graphics.Bitmap
                if (bitmap != null) {
                    val uri = ImageUtil.saveBitmapToUri(this, bitmap)
                    fotoUri = uri
                    mostrarFoto(uri)
                }
            }
        }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraLauncher.launch(intent)
    }

    private val galeriaLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fotoUri = it
                mostrarFoto(it)
            }
        }

    private fun abrirGaleria() {
        galeriaLauncher.launch("image/*")
    }

    private fun mostrarFoto(uri: Uri?) {
        if (uri == null) return
        val input = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input)
        fotoPreview.visibility = View.VISIBLE
        fotoPreview.setImageBitmap(bitmap)
        iconCamera.visibility = View.GONE
        textFoto.visibility = View.GONE
    }

    // ================================================================
    // ENVIAR REPORTE CON BOTE ID REAL
    // ================================================================
    private fun enviarReporte() {
        val tipo = spinnerTipo.selectedItem.toString()
        val index = spinnerContenedor.selectedItemPosition
        val boteId = basurerosIds.getOrNull(index) ?: ""

        val descripcion = edtDescripcion.text.toString()

        if (tipo == "Seleccionar tipo" || boteId.isEmpty() || descripcion.isBlank()) {
            Toast.makeText(this, "Completa los campos obligatorios.", Toast.LENGTH_LONG).show()
            return
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("bote_id", boteId)
            .addFormDataPart("user_id", "user001")
            .addFormDataPart("tipo_problema", tipo)
            .addFormDataPart("mensaje", descripcion)

        fotoUri?.let { uri ->
            val path = RealPathUtil.getRealPath(this, uri)
            if (path != null) {
                val file = File(path)
                builder.addFormDataPart(
                    "foto", file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
            }
        }

        val request = Request.Builder()
           // .url("http://192.168.1.81/ecotec_api/notificaciones/crear.php")
            .url("http://10.247.163.12/ecotec_api/notificaciones/crear.php")
            .post(builder.build())
            .build()

        Thread {
            try {
                client.newCall(request).execute()
                runOnUiThread {
                    Toast.makeText(this, "Reporte enviado ✔", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
