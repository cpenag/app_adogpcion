package com.example.appcynthiapena.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

class Usuario {
    @PrimaryKey(autoGenerate = true)
    var id: Long=0
    var correo: String? = null
    var nombre: String? = null
    var fecha_nac: String? = null
    var password: String? = null

    constructor(correo: String?, nombre: String?, fecha_nac: String?, password: String?) {
        this.correo = correo
        this.nombre = nombre
        this.fecha_nac = fecha_nac
        this.password = password
    }
}