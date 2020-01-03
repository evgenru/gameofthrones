package ru.skillbranch.gameofthrones.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*
import org.koin.core.context.GlobalContext
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.remote.AnApiOfIceAndFire
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.repositories.room.getShortHouseName

object RootRepository {

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable")
        throwable.printStackTrace()
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + errorHandler)

    private val anApiOfIceAndFire: AnApiOfIceAndFire by GlobalContext.get().koin.inject()
    private val database: Database by GlobalContext.get().koin.inject()

    suspend fun sync() {
        val houses = getNeedHouseWithCharacters(*AppConfig.NEED_HOUSES)
        Log.d("HousesInteractor", "loading: houses: ${houses.size}")
        Log.d(
            "HousesInteractor",
            "loading: characters: ${houses.map { it.second }.flatten().size}"
        )
        if (houses.isEmpty()) {
            error("houses is empty")
        } else {
            insertHouses(houses.map { it.first })
            val characters =
                houses.map { it.second }.flatten().distinctBy { it.url }
            insertCharacters(characters)
        }
    }

    suspend fun findAllHouses(): List<House> {
        val houses = database.getHouses()
        return AppConfig.NEED_HOUSES
            .map { getShortHouseName(it) }
            .mapNotNull { houseName ->
                houses.firstOrNull { it.name == houseName }
            }
    }


    /**
     * Получение данных о всех домах из сети
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getAllHouses(): List<HouseRes> {
        return loadAllHouses()
    }

    private suspend fun loadAllHouses(): List<HouseRes> {
        val houses = mutableListOf<HouseRes>()
        var page = 1
        while (true) {
            val response = anApiOfIceAndFire.getHouses(page++)
            val newHouses = if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.d(
                    "RootRepository",
                    "getAllHouses: response.code() = ${response.code()}"
                )
                emptyList()
            }
            if (newHouses.isEmpty())
                break
            houses.addAll(newHouses)
        }
        return houses
    }

    /**
     * Получение данных о требуемых домах по их полным именам из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getNeedHouses(vararg houseNames: String): List<HouseRes> {
        return getAllHouses().filter { house -> house.name in houseNames }
    }

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getNeedHouseWithCharacters(
        vararg houseNames: String
    ): List<Pair<HouseRes, List<CharacterRes>>> {
        val houses = getAllHouses()
        val needHouses = houses.filter { house -> house.name in houseNames }

        val resultHouses = needHouses.map { it to mutableListOf<CharacterRes>() }
        scope.launch {
            resultHouses.forEach {
                it.first.swornMembers.forEach { characterUrl ->
                    val characterId =
                        characterUrl.drop(characterUrl.lastIndexOf('/') + 1)
                    launch {
                        it.second.add(loadCharacter(characterId))
                        Log.d("RootRepository", "loadCharacter: $characterId")
                    }
                }
            }
        }.join()
        return resultHouses
    }

    private suspend fun loadCharacter(characterId: String): CharacterRes {
        val response = anApiOfIceAndFire.getCharacter(characterId)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            Log.d(
                "RootRepository",
                "loadCharacter: response.code() = ${response.code()}"
            )
            error("Character not found")
        }
    }

    /**
     * Запись данных о домах в DB
     * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun insertHouses(houses: List<HouseRes>) {
        database.insertHouses(houses)
    }

    /**
     * Запись данных о пересонажах в DB
     * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun insertCharacters(Characters: List<CharacterRes>) {
        database.insertCharacters(Characters)
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun dropDb() {
        database.dropDb()
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    suspend fun findCharactersByHouseName(name: String): List<CharacterItem> {
        return database.findCharactersByHouseName(name).sortedBy { it.name }
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    suspend fun findCharacterFullById(id: String): CharacterFull = database.findCharacterFullById(id)

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    suspend fun isNeedUpdate() = database.getCountHouses() == 0

}
