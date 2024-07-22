package com.example.appcynthiapena

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appcynthiapena.room.Db
import com.example.appcynthiapena.room.entity.Usuario
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Calendar

class ActivityRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        val room = Room.databaseBuilder(this, Db::class.java,"database-ciisa").allowMainThreadQueries().build()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //REFERENCIAS
        val btn_registro_login = findViewById<Button>(R.id.btn_registro_login)
        val til_nombre_register = findViewById<TextInputLayout>(R.id.til_nombre_register)
        val til_email_register = findViewById<TextInputLayout>(R.id.til_email_register)
        val til_contrasena_register = findViewById<TextInputLayout>(R.id.til_contrasena_register)
        val til_rcontrasena_register = findViewById<TextInputLayout>(R.id.til_rcontrasena_register)
        val til_fnac_register = findViewById<TextInputLayout>(R.id.til_fnac_register)

        //REGISTRAR Y CORUTINA
        btn_registro_login.setOnClickListener {
            var contador = validarCampos()
            if (contador == 0) {
                var nombre = til_nombre_register.editText?.text.toString()
                var correo = til_email_register.editText?.text.toString()
                var contrasena = til_contrasena_register.editText?.text.toString()
                var fecha_nac = til_fnac_register.editText?.text.toString()
                val usuario = Usuario(correo,nombre,fecha_nac,contrasena)
                lifecycleScope.launch {
                    val id = room.daoUsuario().agregarUsuario(usuario)
                    if (id>0){
                        Log.d("IDUSER",id.toString())
                        Toast.makeText(this@ActivityRegistro, "Usuario Registrado. Inicia sesión", Toast.LENGTH_LONG).show()
                        val intent =  Intent(this@ActivityRegistro,ActivityInicioSesion::class.java)
                        startActivity(intent)
                    }
                }
            }else{
                Toast.makeText(this@ActivityRegistro, "Error al agregar usuario", Toast.LENGTH_LONG).show()
            }
        }
        //INSTANCIA CALENDARIO
        val cal = Calendar.getInstance()
//LISTENER
        val listenerFecha = DatePickerDialog.OnDateSetListener { datePicker, anyo, mes, dia ->
            // Ajustamos el mes
            val mesSeleccionado = mes + 1
            var smes = "${mesSeleccionado}"
            var sdia = "${dia}"

            if (mesSeleccionado < 10) {
                smes = "0${mesSeleccionado}"
            }
            if (dia < 10) {
                sdia = "0${dia}"
            }
            //VALIDACION FECHA NO SUPERIOR A LA ACTUAL
            val fechaSeleccionada = "$sdia/$smes/$anyo"

            val fechaSeleccionadaCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, anyo)
                set(Calendar.MONTH, mes)
                set(Calendar.DAY_OF_MONTH, dia)
            }
            val fechaActual = Calendar.getInstance()

            if (fechaSeleccionadaCalendar < fechaActual) {
                til_fnac_register.editText?.setText(fechaSeleccionada)
            } else {
                Toast.makeText(this, "La fecha seleccionada no puede ser igual o superior a la fecha actual", Toast.LENGTH_SHORT).show()
            }
        }

            //EVENTO PICKER - MOSTRAR
            til_fnac_register.editText?.setOnClickListener {
            DatePickerDialog(this, listenerFecha, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }
    //VALIDACIÓN DE CAMPOS
    fun validarCampos():Int{
        var contador:Int = 0
        val til_nombre_register = findViewById<TextInputLayout>(R.id.til_nombre_register)
        val til_rcontrasena_register = findViewById<TextInputLayout>(R.id.til_rcontrasena_register)
        val til_email_register = findViewById<TextInputLayout>(R.id.til_email_register)
        val til_contrasena_register = findViewById<TextInputLayout>(R.id.til_contrasena_register)
        val til_fnac_register = findViewById<TextInputLayout>(R.id.til_fnac_register)
        var nombre = til_nombre_register.editText?.text.toString()
        var correo = til_email_register.editText?.text.toString()
        var contrasena = til_contrasena_register.editText?.text.toString()
        var rcontrasena = til_rcontrasena_register.editText?.text.toString()
        var fecha_nac = til_fnac_register.editText?.text.toString()

        val validate = Validate()
        //VALIDACIONES NOMBRE
        if (validate.validarCampoNulo(nombre)) {
            til_nombre_register.error = "El campo nombre no puede estar vacío"
            contador++
        }
        else{
            if (validate.validarNombre(nombre)) {
                til_nombre_register.error ="El nombre debe contener sólo letras"
                contador++
            }
            else{
                til_nombre_register.error = ""
            }
        }
        //VALIDACIONES EMAIL
        if (validate.validarCampoNulo(correo)) {
            til_email_register.error = "El campo email no puede estar vacío"
            contador++
        }
        else{
            if (validate.validarFormatoCorreo(correo)) {
                til_email_register.error = "El email no cumple formato user@correo.cl"
                contador++
            }
            else{
                til_email_register.error = ""
            }
        }
        //VALIDACIONES FECHA NAC
        if (validate.validarCampoNulo(fecha_nac)) {
            til_fnac_register.error = "El campo fecha de nacimiento no puede estar vacío"
            contador++
        }
        //VALIDACION CONTRASEÑAS
        if (validate.validarCampoNulo(contrasena)) {
            til_contrasena_register.error = "El campo contraseña no puede estar vacío"
            contador++
        }
        else{
            til_contrasena_register.error = ""
        }
        if (validate.validarCamposIguales(contrasena,rcontrasena)) {
            til_contrasena_register.error ="Las contraseñas no coinciden"
            til_rcontrasena_register.error ="Las contraseñas no coinciden"
            contador++
        }

        return contador

    }
}


