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

        // Recibimos el área enviada desde Login
        areaAsignada = intent.getStringExtra("area") ?: ""

        bottomNav = findViewById(R.id.cleanBottomNav)

        // Fragment inicial: HOME
        openFragment(CleanHomeFragment.newInstance(areaAsignada))

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.clean_nav_home ->
                    openFragment(CleanHomeFragment.newInstance(areaAsignada))

                R.id.clean_nav_basureros ->
                    openFragment(CleanBasurerosFragment.newInstance(areaAsignada))

                R.id.clean_nav_historial ->
                    openFragment(CleanHistorialFragment())

                R.id.clean_nav_perfil ->
                    openFragment(CleanPerfilFragment())   // ← ESTE SOLO FUNCIONA SI EXISTE
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
