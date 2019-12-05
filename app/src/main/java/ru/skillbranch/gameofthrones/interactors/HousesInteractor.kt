package ru.skillbranch.gameofthrones.interactors

import ru.skillbranch.gameofthrones.repositories.RootRepository

class HousesInteractor(private val repository: RootRepository) {

    fun loading(complete: () -> Unit, error: (message: String) -> Unit) {
        repository.loading(complete, error)
    }
}
