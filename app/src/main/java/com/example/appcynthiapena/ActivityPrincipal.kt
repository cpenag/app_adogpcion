package com.example.appcynthiapena

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appcynthiapena.room.Db
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ActivityPrincipal : AppCompatActivity() {
    lateinit var arrayAdapterListView: ArrayAdapter<String>
    lateinit var perros: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)
        //inicializar bd
        val room = Room.databaseBuilder(this, Db::class.java, "database-ciisa").allowMainThreadQueries().build()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //obtengo correo
        val correo: String = intent.getStringExtra("correo").toString()
        val tv_bienvenido = findViewById<TextView>(R.id.tv_bienvenido)
        tv_bienvenido.setText("Bienvenido $correo")
        // inicalizacion referencias
        val fab_add = findViewById<FloatingActionButton>(R.id.fab_add)
        val sp_sexo = findViewById<Spinner>(R.id.sp_sexo)
        val lv_datos = findViewById<ListView>(R.id.lv_datos)
        //spinner por defecto con su listviw general // independiente de los  perros ingresador por cadausuario
        val sexoMascotas = arrayListOf("Seleccione opción", "Macho", "Hembra")
        val arrayAdapterSpinner = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_spinner_dropdown_item, sexoMascotas)
        sp_sexo.adapter = arrayAdapterSpinner

        val perros = ArrayList<String>()
        perros.add("Bella")
        perros.add("Buddy")
        perros.add("Coco")
        perros.add("Daisy")
        perros.add("Max")
        val machos = arrayListOf("Buddy", "Max")
        val hembras = arrayListOf("Bella", "Coco", "Daisy")

        arrayAdapterListView = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_list_item_1, perros)
        lv_datos.adapter = arrayAdapterListView
        //LISTENER SPINNER
        sp_sexo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> arrayAdapterListView = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_list_item_1, perros)
                    1 -> arrayAdapterListView = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_list_item_1, machos)
                    2 -> arrayAdapterListView = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_list_item_1, hembras)
                }
                lv_datos.adapter = arrayAdapterListView
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        //LISTENER LISTVIEW  EXTRAIGO NOMBRE E ID
        lv_datos.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val perro = lv_datos.getItemAtPosition(position).toString()
                val perroArray = perro.split(" (").toTypedArray()
                val nombrePerro = perroArray[0]
                val idPerro = perroArray[1].substringBefore(")").toLong()
                val descripcion = room.daoPerro().obtenerDescripcionPorNombre(nombrePerro) ?: "Descripción no encontrada"
                val intent = Intent(this@ActivityPrincipal, ActivityDetalle::class.java)
                intent.putExtra("correo", correo)
                intent.putExtra("mascota", nombrePerro)
                intent.putExtra("id_perro", idPerro)
                intent.putExtra("descripcion", descripcion)
                startActivity(intent)
            }
        }
        // AGREGAR
        fab_add.setOnClickListener {
            val intent = Intent(this@ActivityPrincipal, ActivityAgregar::class.java)
            intent.putExtra("correo", correo)
            startActivity(intent)
        }
        //CORRUTINAS
        lifecycleScope.launch {
            var respuesta = room.daoPerro().obtenerPerrosUsuario(correo)
            perros.clear()
            for (indice in respuesta.indices) {
                perros.add("${respuesta[indice].name_dog} (${respuesta[indice].id})")
            }
            arrayAdapterListView = ArrayAdapter(this@ActivityPrincipal, android.R.layout.simple_list_item_1, perros)
            lv_datos.adapter = arrayAdapterListView
        }
    }
}