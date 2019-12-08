package ru.skillbranch.gameofthrones.repositories

import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface Database {
    suspend fun insertHouses(houses: List<HouseRes>)
    suspend fun insertCharacters(characters: List<CharacterRes>)
    suspend fun dropDb()
    suspend fun findCharactersByHouseName(name: String): List<CharacterItem>
    suspend fun findCharacterFullById(id: String): CharacterFull
    suspend fun getCountHouses(): Int
    suspend fun getHouses():List<House>
}

