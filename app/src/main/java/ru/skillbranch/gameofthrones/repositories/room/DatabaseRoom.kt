package ru.skillbranch.gameofthrones.repositories.room

import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.repositories.Database

class DatabaseRoom(
    private val houseDao: HouseDao,
    private val characterDao: CharacterDao
) : Database {

    override suspend fun insertHouses(houses: List<HouseRes>) {
        houseDao.insert(*houses.map { it.toEntity() }.toTypedArray())
        houseDao.insertRelative(
            *houses.map { house ->
                house.swornMembers.map { member ->
                    RelativeEntity(getIdFromUrl(member), getIdFromUrl(house.url))
                }
            }.flatten().toTypedArray()
        )
    }

    override suspend fun insertCharacters(characters: List<CharacterRes>) {
        characterDao.insert(*characters.map { characterRes ->
            characterRes.toEntity()
        }.toTypedArray())
    }

    override suspend fun dropDb() {
        houseDao.removeAll()
        characterDao.removeAll()
    }

    override suspend fun findCharactersByHouseName(name: String): List<CharacterItem> {
        return characterDao.getByHouseName("%$name%").map { it.toCharacterItem() }
    }

    override suspend fun findCharacterFullById(id: String): CharacterFull {
        return characterDao.getById(id)!!.toCharacterFull(characterDao)
    }

    override suspend fun getCountHouses(): Int = houseDao.getCount()

}

private fun CharacterHouseEntity.toCharacterItem() = CharacterItem(
    id, getShortHouseName(house), name, titles, aliases
)


private fun CharacterHouseEntity.toCharacterFull(characterDao: CharacterDao): CharacterFull {
    val father = characterDao.getById(this.father)
    val mother = characterDao.getById(this.mother)
    return CharacterFull(
        id, name, words, born, died, titles, aliases, getShortHouseName(house),
        father?.let { RelativeCharacter(it.id, it.name, getShortHouseName(it.house)) },
        mother?.let { RelativeCharacter(it.id, it.name, getShortHouseName(it.house)) }
    )
}

private fun getIdFromUrl(url: String) = url.drop(url.lastIndexOf('/') + 1)

private fun CharacterRes.toEntity(): CharacterEntity {
    return CharacterEntity(
        getIdFromUrl(url),
        name,
        born,
        died,
        titles,
        aliases,
        father = getIdFromUrl(father),
        mother = getIdFromUrl(mother)
    )
}

private fun HouseRes.toEntity() = HouseEntity(
    getIdFromUrl(url),
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

private fun getShortHouseName(houseName: String) =
    houseName.replace("^House ".toRegex(), "").replace(" of .*".toRegex(), "")



