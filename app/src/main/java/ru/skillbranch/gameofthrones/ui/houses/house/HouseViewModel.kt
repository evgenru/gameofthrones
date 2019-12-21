package ru.skillbranch.gameofthrones.ui.houses.house

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.repositories.RootRepository

class HouseViewModel(private val house: HouseType) : ViewModel(), KoinComponent {
    private val repository: RootRepository by inject()
    private val queryStr = MutableLiveData<String>("")

    fun getCharacters(): LiveData<List<CharacterItem>> =
        MediatorLiveData<List<CharacterItem>>().apply {
            viewModelScope.launch(Dispatchers.IO) {
                val characters = repository.findCharactersByHouseName(house.title)
                withContext(Dispatchers.Main) {
                    addSource(queryStr) { query ->
                        postValue(
                            if (query.isEmpty()) characters
                            else characters.filter { it.name.contains(query, true) }
                        )
                    }
                }
            }
        }

    fun handleSearchQuery(str: String) {
        queryStr.value = str
    }

}

class HouseViewModelFactory(private val house: HouseType) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HouseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HouseViewModel(house) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
