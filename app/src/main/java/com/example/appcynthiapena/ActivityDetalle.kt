package com.example.appcynthiapena

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appcynthiapena.room.Db
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ActivityDetalle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle)
        // Inicialización de la base de datos
        val room =
            Room.databaseBuilder(this, Db::class.java, "database-ciisa").allowMainThreadQueries()
                .build()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //  referencias
        val btn_editar = findViewById<Button>(R.id.btn_editar)
        val btn_eliminar = findViewById<Button>(R.id.btn_eliminar)
        val til_nombre_perro = findViewById<TextInputLayout>(R.id.til_perro_edit)
        val til_descripcion = findViewById<TextInputLayout>(R.id.til_descripcion_edit)
        val tw_id = findViewById<TextView>(R.id.tw_id)

        // datos DESDE INTERFAZ PRINCIPALÑ
        val correo: String = intent.getStringExtra("correo").toString()
        val mascota: String = intent.getStringExtra("mascota").toString()
        val idPerro: Long = intent.getLongExtra(
            "id_perro",
            -1
        ) // -1 en caso de no encontrar el id del perrito

        //  datos en los elementos de la interfaz
        til_nombre_perro.editText?.setText(mascota)
        val descripcion: String = intent.getStringExtra("descripcion").toString()
        til_descripcion.editText?.setText(descripcion)
        tw_id.text = idPerro.toString()

        //  eliminar
        btn_eliminar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Está seguro que desea eliminar el registro?")
            builder.setPositiveButton("Eliminar") { dialog, which ->
                val id = idPerro // Obtener el ID del perro desde el TextView
                lifecycleScope.launch {
                    val respuesta = room.daoPerro().eliminarPerro(id)
                    println(respuesta)
                    Toast.makeText(this@ActivityDetalle, "Elemento eliminado", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@ActivityDetalle, ActivityPrincipal::class.java)
                    intent.putExtra("correo", correo)
                    startActivity(intent)
                }
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }

        //Editar
        btn_editar.setOnClickListener {
            var mascota = til_nombre_perro.editText?.text.toString()
            var descripcion = til_descripcion.editText?.text.toString()
            val id = idPerro
            lifecycleScope.launch {
                val respuesta = room.daoPerro().actualizarPerro(mascota, descripcion, id.toLong())
                println(respuesta)
                val intent = Intent(this@ActivityDetalle, ActivityPrincipal::class.java)
                intent.putExtra("correo", correo)
                startActivity(intent)
            }
        }
    }
}
