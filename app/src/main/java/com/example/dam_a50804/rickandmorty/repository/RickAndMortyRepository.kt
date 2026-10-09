package com.example.dam_a50804.rickandmorty.repository

import com.example.dam_a50804.rickandmorty.model.CharacterResponse
import com.example.dam_a50804.rickandmorty.model.EpisodeResponse
import com.example.dam_a50804.rickandmorty.model.LocationResponse
import com.example.dam_a50804.rickandmorty.network.RetrofitClient

class RickAndMortyRepository {
    private val api = RetrofitClient.apiService

    suspend fun getCharacters(name: String, page: Int): Result<CharacterResponse> {
        return try {
            val response = api.getCharacters(name, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body) else Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEpisodes(name: String, page: Int): Result<EpisodeResponse> {
        return try {
            val response = api.getEpisodes(name, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body) else Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocations(name: String, page: Int): Result<LocationResponse> {
        return try {
            val response = api.getLocations(name, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body) else Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
