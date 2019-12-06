package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters


@androidx.room.Database(
    entities = [HouseEntity::class, CharacterEntity::class, RelativeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RootDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
    abstract fun characterDao(): CharacterDao
}

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> = value.split(ARRAY_SEPARATOR)

    @TypeConverter
    fun fromArrayList(list: List<String>) = list.joinToString(ARRAY_SEPARATOR)

    companion object {
        private const val ARRAY_SEPARATOR = "^#$&%"
    }
}