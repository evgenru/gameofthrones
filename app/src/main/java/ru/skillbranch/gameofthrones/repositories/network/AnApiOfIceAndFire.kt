package ru.skillbranch.gameofthrones.repositories.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface AnApiOfIceAndFire {
    @GET("houses")
    suspend fun getHouses(@Query("page") page: Int, @Query("pageSize") pageSize: Int = 50): Response<List<HouseRes>>

    @GET("characters/{id}")
    suspend fun getCharacter(@Path("id") id: String): Response<CharacterRes>
}



