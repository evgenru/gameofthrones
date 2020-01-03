package ru.skillbranch.gameofthrones.data.remote

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.gameofthrones.AppConfig

object NetworkService {
    val api: AnApiOfIceAndFire by lazy {
        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
            .build()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(AppConfig.BASE_URL)
            .build()

        retrofit.create(AnApiOfIceAndFire::class.java)
    }
}