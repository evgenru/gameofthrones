package ru.skillbranch.gameofthrones.repositories.network

import retrofit2.Response
import retrofit2.http.GET
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface AnApiOfIceAndFire {
    @GET("houses")
    suspend fun getHouses(): Response<List<HouseRes>>

}



