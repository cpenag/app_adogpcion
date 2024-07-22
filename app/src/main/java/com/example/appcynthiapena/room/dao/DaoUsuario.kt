package com.example.appcynthiapena.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcynthiapena.room.entity.Usuario

@Dao
interface DaoUsuario {
    @Query("SELECT * FROM Usuario")
    fun obtenerUsuarios(): List<Usuario>

    @Query("SELECT * FROM Usuario WHERE correo=:correo")
    fun obtenerUsuario(correo: String): List<Usuario>

    @Query("SELECT * FROM Usuario WHERE correo=:correo AND password=:contrasena")
    fun login(correo: String, contrasena: String): List<Usuario>

    @Insert
    fun agregarUsuario(usuario: Usuario): Long
}