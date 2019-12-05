package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.RoomDatabase

@androidx.room.Database(
    entities = [HouseEntity::class, CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RootDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
    abstract fun characterDao(): CharacterDao
}