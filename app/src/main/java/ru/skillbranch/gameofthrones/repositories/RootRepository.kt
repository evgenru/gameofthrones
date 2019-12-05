package ru.skillbranch.gameofthrones.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.repositories.network.AnApiOfIceAndFire

object RootRepository {

    private val anApiOfIceAndFire: AnApiOfIceAndFire by GlobalContext.get().koin.inject()

    fun loading(complete: () -> Unit, error: (message: String) -> Unit) {
        isNeedUpdate { isNeedUpdate ->
            if (isNeedUpdate) {
                getNeedHouseWithCharacters(*AppConfig.NEED_HOUSES) { houses ->
                    Log.d("HousesInteractor", "loading: houses: ${houses.size}")
                    Log.d(
                        "HousesInteractor",
                        "loading: characters: ${houses.map { it.second }.flatten().size}"
                    )

                    if (houses.isEmpty()) {
                        error("houses is empty")
                    } else {
                        insertHouses(houses.map { it.first }) {
                            insertCharacters(houses.map { it.second }.flatten()) {
                                complete()
                            }
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5_000)
                    complete()
                }
            }
        }

    }


    /**
     * Получение данных о всех домах
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                result(loadAllHouses())
            } catch (t: Throwable) {
                result(emptyList())
            }
        }
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
     * Получение данных о требуемых домах по их полным именам (например фильтрация всех домов)
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
        getAllHouses {
            result(
                it.filter { house -> house.name in houseNames }
            )
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharacters(
        vararg houseNames: String,
        result: (houses: List<Pair<HouseRes, List<CharacterRes>>>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            getAllHouses { houses ->
                val needHouses = houses.filter { house -> house.name in houseNames }
                CoroutineScope(Dispatchers.IO).launch {
                    val resultHouses = needHouses.map { it to mutableListOf<CharacterRes>() }
                    launch {
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
                        Log.d("RootRepository", "loadCharacter: started")
                    }.join()
                    result(resultHouses)
                }
            }
        }
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
    fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
        complete()
        //TODO implement me
    }

    /**
     * Запись данных о пересонажах в DB
     * @param characters - Список персонажей (модель CharacterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(characters: List<CharacterRes>, complete: () -> Unit) {
        complete()
        //TODO implement me
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        //TODO implement me
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name: String, result: (Characters: List<CharacterItem>) -> Unit) {
        //TODO implement me
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id: String, result: (Character: CharacterFull) -> Unit) {
        //TODO implement me
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        result(true)
        //TODO implement me
    }

}