package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CharacterDao :
    BaseDao<CharacterEntity> {

    @Query("DELETE FROM CharacterEntity")
    fun removeAll()

    @Query("""SELECT c.*, h.name as house, h.words
        FROM CharacterEntity as c
          INNER JOIN RelativeEntity AS r ON c.id == r.id
          INNER JOIN HouseEntity AS h ON h.id == r.house
        WHERE c.id = :id""")
    fun getById(id: String): CharacterHouseEntity?

    @Query("""SELECT c.*, h.name as house, h.words
        FROM CharacterEntity as c
          INNER JOIN RelativeEntity AS r ON c.id == r.id
          INNER JOIN HouseEntity AS h ON h.id == r.house
        WHERE h.name like :houseName""")
    fun getByHouseName(houseName: String): List<CharacterHouseEntity>


}