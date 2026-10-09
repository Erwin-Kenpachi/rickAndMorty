package com.example.dam_a50804.rickandmorty.network

import com.example.dam_a50804.rickandmorty.model.CharacterResponse
import com.example.dam_a50804.rickandmorty.model.EpisodeResponse
import com.example.dam_a50804.rickandmorty.model.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("name") name: String = "",
        @Query("page") page: Int = 1
    ): Response<CharacterResponse>

    @GET("episode")
    suspend fun getEpisodes(
        @Query("name") name: String = "",
        @Query("page") page: Int = 1
    ): Response<EpisodeResponse>

    @GET("location")
    suspend fun getLocations(
        @Query("name") name: String = "",
        @Query("page") page: Int = 1
    ): Response<LocationResponse>
}
