package com.example.calculadoradecables

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        //setContentView(R.layout.pantalla_inicio_sesion)
        //setContentView(R.layout.pantalla_menu_principal)
        //setContentView(R.layout.pantalla_listado_cables)
        //setContentView(R.layout.listado_cables)
        //setContentView(R.layout.pantalla_datos_cable1)
        //setContentView(R.layout.pantalla_datos_cable2)
        //setContentView(R.layout.pantalla_datos_cable3)
        setContentView(R.layout.pantalla_datos_cable4)
    }
}