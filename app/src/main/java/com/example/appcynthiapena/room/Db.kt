package com.example.appcynthiapena.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcynthiapena.room.dao.DaoPerro
import com.example.appcynthiapena.room.dao.DaoUsuario
import com.example.appcynthiapena.room.entity.Perro
import com.example.appcynthiapena.room.entity.Usuario

@Database(
    entities = [Usuario::class, Perro::class],
    version = 1
)

abstract class Db:RoomDatabase() {
    abstract fun daoUsuario():DaoUsuario
    abstract fun daoPerro():DaoPerro
}