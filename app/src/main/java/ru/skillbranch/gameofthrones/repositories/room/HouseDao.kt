package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Dao

@Dao
interface HouseDao :
    BaseDao<HouseEntity> {
//    @Query("SELECT * FROM HouseEntity")
//    fun getAll(): List<HouseEntity>
}