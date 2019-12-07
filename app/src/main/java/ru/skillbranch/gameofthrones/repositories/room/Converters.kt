package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> = value.split(ARRAY_SEPARATOR)

    @TypeConverter
    fun fromArrayList(list: List<String>) = list.joinToString(ARRAY_SEPARATOR)

    companion object {
        private const val ARRAY_SEPARATOR = "^#$&%"
    }
}