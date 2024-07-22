package com.example.appcynthiapena

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appcynthiapena.room.Db
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ActivityInicioSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)
        //INICIALIZAMOS LA DB
        val room = Room.databaseBuilder(this,Db::class.java,"database-ciisa").allowMainThreadQueries().build()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //widget
        val til_email_login = findViewById<TextInputLayout>(R.id.til_email_login)
        val til_contrasena_login = findViewById<TextInputLayout>(R.id.til_contrasena_login)
        val sw_recordar = findViewById<Switch>(R.id.sw_recordar)
        val btn_ingreso_login = findViewById<Button>(R.id.btn_ingreso_login)


        val preferencias = getSharedPreferences("datos",Context.MODE_PRIVATE)
        til_email_login.editText?.setText(preferencias.getString("correo", ""))

        //OBTENER valores boton ingreso Y CORUTINA
        btn_ingreso_login.setOnClickListener {
            var correo = til_email_login.editText?.text.toString()
            var contrasena = til_contrasena_login.editText?.text.toString()
            var contador = validarCampos()
            var isRemember = sw_recordar.isChecked
            if (contador == 0) {
                if(isRemember){
                    val editor = preferencias.edit()
                    editor.putString("correo",correo)
                    editor.commit()
                }
                lifecycleScope.launch {
                    val response = room.daoUsuario().login(correo, contrasena)
                    if(response.size==1){
                        Toast.makeText(this@ActivityInicioSesion, "Login exitoso", Toast.LENGTH_LONG).show()
                        val intent =  Intent(this@ActivityInicioSesion,ActivityPrincipal::class.java)
                        intent.putExtra("correo",correo)
                        startActivity(intent)
                    }else{
                        til_email_login.error = "Usuario o contraseña inválido"
                        til_contrasena_login.error = "Usuario o contraseña inválido"
                    }
                }

            }

        }
    }
    //VALIDACIÓN DE CAMPOS
    fun validarCampos():Int{
        var contador:Int = 0
        val til_email_login = findViewById<TextInputLayout>(R.id.til_email_login)
        val til_contrasena_login = findViewById<TextInputLayout>(R.id.til_contrasena_login)
        var correo = til_email_login.editText?.text.toString()
        var contrasena = til_contrasena_login.editText?.text.toString()
        val validate = Validate()
        //VALIDACION CORREO
        if (validate.validarCampoNulo(correo)) {
            til_email_login.error = "El campo email no puede estar vacío"
            contador++
        }
        else{
            if (validate.validarFormatoCorreo(correo)) {
                til_email_login.error = "El email no cumple formato user@correo.cl"
                contador++
            }
            else{
                til_email_login.error = ""
            }
        }
        //VALIDACION CONTRASEÑA
        if (validate.validarCampoNulo(contrasena)) {
            til_contrasena_login.error = "El campo contraseña no puede estar vacío"
            contador++
        }
        else{
            til_contrasena_login.error = ""
        }
        return contador
    }
}