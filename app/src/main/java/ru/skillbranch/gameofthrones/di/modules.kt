package ru.skillbranch.gameofthrones.di

import androidx.room.Room
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.interactors.HousesInteractor
import ru.skillbranch.gameofthrones.repositories.Database
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.repositories.network.AnApiOfIceAndFire
import ru.skillbranch.gameofthrones.repositories.room.DatabaseRoom
import ru.skillbranch.gameofthrones.repositories.room.RootDatabase

val dataModule = module {

    single { Room.databaseBuilder(get(), RootDatabase::class.java, "1-master-db").build() }

    single {
        DatabaseRoom(
            get<RootDatabase>(RootDatabase::class.java).houseDao(),
            get<RootDatabase>(RootDatabase::class.java).characterDao()
        ) as Database
    }

    single {
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AnApiOfIceAndFire::class.java)
    }


    single { HousesInteractor(RootRepository) }

}