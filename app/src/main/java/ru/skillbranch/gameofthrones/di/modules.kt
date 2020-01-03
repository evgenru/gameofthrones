package ru.skillbranch.gameofthrones.di

import androidx.room.Room
import org.koin.dsl.module
import ru.skillbranch.gameofthrones.data.remote.NetworkService
import ru.skillbranch.gameofthrones.repositories.Database
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.repositories.room.DatabaseRoom
import ru.skillbranch.gameofthrones.repositories.room.RootDatabase

val dataModule = module {

    single { Room.databaseBuilder(get(), RootDatabase::class.java, "3-master-db").build() }

    single {
        DatabaseRoom(
            get<RootDatabase>(RootDatabase::class.java).houseDao(),
            get<RootDatabase>(RootDatabase::class.java).characterDao()
        ) as Database
    }

    single {
        NetworkService.api
    }

    single { RootRepository }

}