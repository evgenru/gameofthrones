package ru.skillbranch.gameofthrones.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharactersListViewModel : ViewModel() {

    private val _characters = MutableLiveData<List<CharacterItem>>()
    val characters: LiveData<List<CharacterItem>> = _characters

    fun setHouseName(houseName: String) {
        RootRepository.findCharactersByHouseName(houseName){
            _characters.postValue(it)
        }
    }
}