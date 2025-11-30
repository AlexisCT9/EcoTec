package com.example.ecotec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DBTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbtest)

        // --- JSON de prueba ---
        val jsonData = """
            {
                "bote_id": "bote001",
                "user_id": "user001",
                "tipo_residuo": "Plástico",
                "mensaje": "Prueba desde DBTestActivity"
            }
        """.trimIndent()

        // --- LLAMADO CORRECTO ---
        DBConnection.enviarJSON("notificaciones/crear.php", jsonData) { exito: Boolean, mensaje: String ->
            if (exito) {
                println("✔ EXITO: $mensaje")
            } else {
                println("❌ ERROR: $mensaje")
            }
        }
    }
}
