package ru.skillbranch.gameofthrones.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.repositories.RootRepository

class PageViewModel : ViewModel() {

    private val _house = MutableLiveData<List<CharacterItem>>()
    val text: LiveData<String> = Transformations.map(_house) {
        "Hello world from section: ${it.size}"
    }

    fun setHouseName(houseName: String) {
        RootRepository.findCharactersByHouseName(houseName){
            _house.postValue(it)
        }
    }
}