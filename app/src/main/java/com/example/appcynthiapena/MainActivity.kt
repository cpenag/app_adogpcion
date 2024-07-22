package com.example.appcynthiapena

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_registro= findViewById<Button>(R.id.btn_registro)

        btn_login.setOnClickListener{
            val intent =  Intent(this@MainActivity,ActivityInicioSesion::class.java)
            startActivity(intent)
        }
        btn_registro.setOnClickListener{
            val intent = Intent(this@MainActivity,ActivityRegistro::class.java)
            startActivity(intent)
        }
    }

}