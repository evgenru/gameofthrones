package ru.skillbranch.gameofthrones.ui.character

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharacterViewModel(private val characterId: String) : ViewModel(), KoinComponent {

    private val rootRepository: RootRepository by inject()

    private val _character = MutableLiveData<CharacterFull>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            postValue(
                rootRepository.findCharacterFullById(characterId)
            )
        }
    }

    fun getCharacter(): LiveData<CharacterFull> = _character
}


class CharacterViewModelFactory(private val characterId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterViewModel(characterId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}