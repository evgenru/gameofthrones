package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class HouseDao :
    BaseDao<HouseEntity> {

    @Query("SELECT * FROM HouseEntity")
    abstract fun getAll(): List<HouseEntity>

    fun removeAll() {
        removeAllHouses()
        removeAllRelatives()
    }

    @Query("DELETE FROM HouseEntity")
    abstract fun removeAllHouses()

    @Query("DELETE FROM RelativeEntity")
    abstract fun removeAllRelatives()

    @Query("SELECT count(*) FROM HouseEntity")
    abstract fun getCount(): Int

    @Insert
    abstract fun insertRelative(vararg relative: RelativeEntity)
}

