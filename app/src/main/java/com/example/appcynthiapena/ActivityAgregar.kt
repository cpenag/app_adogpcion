package com.example.appcynthiapena

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.appcynthiapena.room.Db
import com.example.appcynthiapena.room.entity.Perro
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ActivityAgregar : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        private val CAMERA_PERMISSION_REQUEST_CODE = 2
        val GALLERY_PERMISSION_REQUEST_CODE = 1001
        val GALLERY_REQUEST_CODE = 1002
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar)
        //INICIALIZAMOS LA DB
        val room = Room.databaseBuilder(this,Db::class.java,"database-ciisa").allowMainThreadQueries().build()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //REFERENCIAS
        val btn_guardar_add = findViewById<Button>(R.id.btn_guardar_add)
        val btn_capture = findViewById<FloatingActionButton>(R.id. btn_capture)
        val fab_gallery = findViewById<FloatingActionButton>(R.id. fab_gallery)
        val til_name_perro = findViewById<TextInputLayout>(R.id.til_name_perro)
        val til_descripcion_perro = findViewById<TextInputLayout>(R.id.til_descripcion_perro)

        val correo:String = intent.getStringExtra("correo").toString()

        btn_capture.setOnClickListener {
            checkCameraPermission()
        }
        fab_gallery.setOnClickListener {
            requestGalleryPermission()
        }
        // GUARDAR perrito y su CORUTINA
        btn_guardar_add.setOnClickListener{
            var nombre_perro = til_name_perro.editText?.text.toString()
            var  descripcion_perro= til_descripcion_perro.editText?.text.toString()
            val perro = Perro(nombre_perro,descripcion_perro,correo)
            lifecycleScope.launch {
                val id = room.daoPerro().agregarPerro(perro)
                // ver info en log de todos los perros
                var respuesta = room.daoPerro().obtenerPerros()
                for (elemento in respuesta){
                    println(elemento.toString())
                }
                if (id>0){
                    Toast.makeText(this@ActivityAgregar, "Perro registrado exitosamente", Toast.LENGTH_SHORT).show()
                    val intent =  Intent(this@ActivityAgregar,ActivityPrincipal::class.java)
                    intent.putExtra("correo",correo)
                    startActivity(intent)
                }
            }


        }

    }
    // METODO QUE VALIDA EL PERMISO DE LA CAMARA EN CASO DE TENER PERMISO EJECUTARA EL INTENT DE LA FOTO
    fun checkCameraPermission () {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission. CAMERA) !=
            PackageManager. PERMISSION_GRANTED) {
        // Solicitar permiso
            ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission. CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE )
        } else {
        // inicia la cámara
            dispatchTakePictureIntent()
        }
    }
    //METODO PERMISO LECTURA IMAGEN
    fun requestGalleryPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, solicítalo
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_REQUEST_CODE)
        } else {
            //
            openGallery()
        }
    }

    // METODO QUE GATILLA LA CAPTURA DE LA IMAGEN
    private fun dispatchTakePictureIntent () {
        Intent(MediaStore. ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity( packageManager)?.also {
                startActivityForResult(takePictureIntent , REQUEST_IMAGE_CAPTURE )
            }
        }
    }
    //función maneja las respuestas del usuario
    override fun onRequestPermissionsResult (requestCode: Int , permissions: Array< out String>,
                                             grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> { //CAMARA
                if (grantResults. isNotEmpty() && grantResults[ 0] == PackageManager. PERMISSION_GRANTED)
                {
                // Permiso cámara
                    dispatchTakePictureIntent()
                } else {
                // Permiso denegado.
                    Toast.makeText( this, "Permiso de cámara denegado" , Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_PERMISSION_REQUEST_CODE -> { //GALERIA
                if (grantResults. isNotEmpty() && grantResults[ 0] == PackageManager. PERMISSION_GRANTED)
                {
                    // Permiso galeria
                    openGallery()
                } else {
                    // Permiso denegado.
                    Toast.makeText( this, "Permiso de galería denegado" , Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        }
    }
    //RESULTADO DE ACTIVIDAD INICIADA
    override fun onActivityResult (requestCode: Int , resultCode: Int , data: Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity. RESULT_OK) {
            val imageBitmap = data?. extras?.get("data") as Bitmap
            val imv_foto = findViewById<ImageView>(R.id. imv_foto)
            imv_foto.setImageBitmap(imageBitmap)
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity. RESULT_OK) {
            val imageUri = data?. data
            val imv_foto = findViewById<ImageView>(R.id. imv_foto)
            imv_foto.setImageURI(imageUri)
        }
    }
    //FUNCION ABRIR GALERIA
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
}