package com.example.dam_a50804.rickandmorty.network

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val apiService: RickAndMortyApiService by lazy {
        retrofit.create(RickAndMortyApiService::class.java)
    }
}
