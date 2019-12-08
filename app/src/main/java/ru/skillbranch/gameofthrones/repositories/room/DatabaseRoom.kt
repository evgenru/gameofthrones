package ru.skillbranch.gameofthrones.repositories.room

import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
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
                    RelativeEntity(member.getLastSegment(), house.url.getLastSegment())
                }
            }.flatten().toTypedArray()
        )
    }

    override suspend fun getHouses(): List<House> {
        return houseDao.getAll().map { it.toHouse() }
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
        return characterDao.getByHouseName(name).map { it.toCharacterItem() }
    }

    override suspend fun findCharacterFullById(id: String): CharacterFull {
        return characterDao.getById(id)!!.toCharacterFull(characterDao)
    }

    override suspend fun getCountHouses(): Int = houseDao.getCount()

}

private fun HouseEntity.toHouse(): House {
    return House(
        id,
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
}

private fun CharacterWithHouse.toCharacterItem() = CharacterItem(
    id, house, name, titles, aliases
)


private fun CharacterWithHouse.toCharacterFull(characterDao: CharacterDao): CharacterFull {
    val father = characterDao.getById(this.father)
    val mother = characterDao.getById(this.mother)
    return CharacterFull(
        id, name, words, born, died, titles, aliases, house,
        father?.let { RelativeCharacter(it.id, it.name, it.house) },
        mother?.let { RelativeCharacter(it.id, it.name, it.house) }
    )
}


private fun String.getLastSegment(delimiters: String = "/") = this.split(delimiters).last()

private fun CharacterRes.toEntity(): CharacterEntity {
    return CharacterEntity(
        url.getLastSegment(),
        name,
        born,
        died,
        titles,
        aliases,
        father = father.getLastSegment(),
        mother = mother.getLastSegment()
    )
}

private fun HouseRes.toEntity() = HouseEntity(
    url.getLastSegment(),
    getShortHouseName(name),
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

fun getShortHouseName(houseName: String) =
    houseName.replace("^House ".toRegex(), "").replace(" of .*".toRegex(), "")



