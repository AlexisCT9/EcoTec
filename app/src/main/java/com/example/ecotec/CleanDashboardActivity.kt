package com.example.ecotec

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CleanDashboardActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var areaAsignada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_dashboard)

        // Recibimos el área desde Login (solo para mostrar)
        areaAsignada = intent.getStringExtra("area") ?: ""

        bottomNav = findViewById(R.id.cleanBottomNav)

        // Home inicial
        openFragment(CleanHomeFragment.newInstance(areaAsignada))

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.clean_nav_home ->
                    openFragment(CleanHomeFragment.newInstance(areaAsignada))

                R.id.clean_nav_basureros ->
                    openFragment(CleanBasurerosFragment())   // ✔ SIN newInstance

                R.id.clean_nav_historial ->
                    openFragment(CleanHistorialFragment())

                R.id.clean_nav_perfil ->
                    openFragment(CleanPerfilFragment())
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cleanFragmentContainer, fragment)
            .commit()
    }
}
