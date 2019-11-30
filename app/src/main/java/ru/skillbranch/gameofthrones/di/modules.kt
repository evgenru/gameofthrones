package ru.skillbranch.gameofthrones.di

import androidx.room.Room
import org.koin.dsl.module
import ru.skillbranch.gameofthrones.interactors.HousesInteractor

val dataModule = module {

//    single { Room.databaseBuilder(get(), AppDatabase::class.java, "mylist-master-db").build() }
//
//    single { TaskRepository(RoomTaskDataSource(get())) }

    single { HousesInteractor() }

}