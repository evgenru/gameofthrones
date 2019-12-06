package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id","house"])
data class RelativeEntity(
    val id: String,
    val house: String
)