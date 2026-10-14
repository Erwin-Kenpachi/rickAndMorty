package com.example.dam_a50804.rickandmorty.repository

import com.example.dam_a50804.rickandmorty.model.CharacterResponse
import com.example.dam_a50804.rickandmorty.model.EpisodeResponse
import com.example.dam_a50804.rickandmorty.model.LocationResponse
import com.example.dam_a50804.rickandmorty.network.RetrofitClient
import retrofit2.HttpException

class RickAndMortyRepository {
    private val api = RetrofitClient.apiService

    suspend fun getCharacters(name: String, page: Int): CharacterResponse {
        val response = api.getCharacters(name, page)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getEpisodes(name: String, page: Int): EpisodeResponse {
        val response = api.getEpisodes(name, page)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getLocations(name: String, page: Int): LocationResponse {
        val response = api.getLocations(name, page)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw HttpException(response)
        }
    }
}
