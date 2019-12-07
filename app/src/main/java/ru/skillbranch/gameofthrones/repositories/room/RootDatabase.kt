package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.RoomDatabase
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

