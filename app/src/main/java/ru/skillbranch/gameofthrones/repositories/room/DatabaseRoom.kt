package ru.skillbranch.gameofthrones.repositories.room

import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.repositories.Database

class DatabaseRoom(val houseDao: HouseDao, val characterDao: CharacterDao) : Database {

    override suspend fun insertHouses(houses: List<HouseRes>) {
        houseDao.insert(*houses.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun insertCharacters(characters: List<CharacterRes>) {
        houseDao.insert(*characters.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun dropDb() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findCharactersByHouseName(name: String): List<CharacterItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findCharacterFullById(id: String): CharacterFull {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getCountHouses(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private fun CharacterRes.toEntity() = CharacterEntity (
    url.drop(url.lastIndexOf('/') + 1),
    name,
    this.
)

private fun HouseRes.toEntity() = HouseEntity(
    name,
    name,
    region,
    coatOfArms,
    words,
    titles,
    seats,
    currentLord,
    heir,
    overlord,
    founded,
    founder,
    diedOut,
    ancestralWeapons
)


