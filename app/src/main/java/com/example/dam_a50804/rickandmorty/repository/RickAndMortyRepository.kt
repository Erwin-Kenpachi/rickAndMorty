package com.example.dam_a50804.rickandmorty.repository

import com.example.dam_a50804.rickandmorty.model.CharacterResponse
import com.example.dam_a50804.rickandmorty.model.EpisodeResponse
import com.example.dam_a50804.rickandmorty.model.LocationResponse
import com.example.dam_a50804.rickandmorty.network.RetrofitClient

class RickAndMortyRepository {
    private val api = RetrofitClient.apiService

    suspend fun getCharacters(name: String, page: Int): Result<CharacterResponse> {
        return runCatching {
            val response = api.getCharacters(name, page)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty body")
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        }
    }

    suspend fun getEpisodes(name: String, page: Int): Result<EpisodeResponse> {
        return runCatching {
            val response = api.getEpisodes(name, page)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty body")
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        }
    }

    suspend fun getLocations(name: String, page: Int): Result<LocationResponse> {
        return runCatching {
            val response = api.getLocations(name, page)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty body")
            } else {
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        }
    }
}
