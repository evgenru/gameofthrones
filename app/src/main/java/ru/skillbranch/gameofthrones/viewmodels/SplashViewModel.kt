package ru.skillbranch.gameofthrones.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.skillbranch.gameofthrones.interactors.HousesInteractor

class SplashViewModel : ViewModel(), KoinComponent {

    private val housesInteractor: HousesInteractor by inject()

    private val _data: MutableLiveData<Data> = MutableLiveData(Progress(0))
    val data: LiveData<Data> = _data

    init {
        housesInteractor.loading{
            _data.postValue(Progress(it))
        }
    }
}

sealed class Data

data class Progress(val progress: Int) : Data()
data class Error(val message: String) : Data()
class Done : Data()
