package ru.skillbranch.gameofthrones.interactors

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.repositories.RootRepository

class HousesInteractor(private val repository: RootRepository) {

    fun loading(result: (progress: Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllHouses {
                Log.d("HousesInteractor", "loading: houses: ${it.size}")
                result(10)
            }
        }
    }

}
