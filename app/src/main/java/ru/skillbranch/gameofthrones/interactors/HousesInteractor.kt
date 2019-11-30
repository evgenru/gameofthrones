package ru.skillbranch.gameofthrones.interactors

import ru.skillbranch.gameofthrones.repositories.RootRepository

class HousesInteractor(private val repository: RootRepository) {

    fun loading() {
        repository.getAllHouses {

        }
    }

}
