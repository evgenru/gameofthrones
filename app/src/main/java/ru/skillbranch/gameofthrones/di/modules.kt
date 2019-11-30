package ru.skillbranch.gameofthrones.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.interactors.HousesInteractor
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.repositories.network.AnApiOfIceAndFire

val dataModule = module {

    //    single { Room.databaseBuilder(get(), AppDatabase::class.java, "mylist-master-db").build() }
//
//    single { TaskRepository(RoomTaskDataSource(get())) }

    single {
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AnApiOfIceAndFire::class.java)
    }


    single { HousesInteractor(RootRepository) }

}