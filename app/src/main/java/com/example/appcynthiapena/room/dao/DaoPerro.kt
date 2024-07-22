package com.example.appcynthiapena.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcynthiapena.room.entity.Perro

@Dao
interface DaoPerro {
    @Query("SELECT * FROM Perro")
    fun obtenerPerros() : List<Perro>

    @Query("SELECT * FROM Perro WHERE user=:user")
    fun obtenerPerrosUsuario (user:String): List<Perro>

    @Query("SELECT * FROM Perro WHERE name_dog=:name AND user=:user")
    fun obtenerPerroPorNombre (name: String, user: String): List<Perro>

    @Query("SELECT description_dog FROM Perro WHERE name_dog = :name")
    fun obtenerDescripcionPorNombre(name: String): String

    @Insert
    fun agregarPerro (perro: Perro): Long

    @Query ("UPDATE Perro SET name_dog = :name, description_dog=:descripcion WHERE id=:id")
    fun actualizarPerro (name: String, descripcion: String, id: Long): Int

    @Query("DELETE FROM Perro where id=:id")
    fun eliminarPerro (id: Long)
}
