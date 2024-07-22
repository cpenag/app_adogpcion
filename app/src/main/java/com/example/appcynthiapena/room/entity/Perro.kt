package com.example.appcynthiapena.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Perro {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name_dog: String? = null
    var description_dog: String? = null
    var user: String? = null

    constructor(name_dog: String?, description_dog: String?, user: String?) {
        this.name_dog = name_dog
        this.description_dog = description_dog
        this.user = user
    }
}

