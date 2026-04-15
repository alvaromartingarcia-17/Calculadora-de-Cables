package com.example.calculadoradecables

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        //setContentView(R.layout.pantalla_inicio_sesion)
        //setContentView(R.layout.pantalla_menu_principal)
        setContentView(R.layout.pantalla_listado_cables)
        //setContentView(R.layout.listado_cables)
    }
}