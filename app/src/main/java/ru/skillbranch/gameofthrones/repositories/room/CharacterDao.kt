package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Dao

@Dao
interface CharacterDao :
    BaseDao<CharacterEntity> {
//    @Query("SELECT * FROM HouseEntity")
//    fun getAll(): List<HouseEntity>
}