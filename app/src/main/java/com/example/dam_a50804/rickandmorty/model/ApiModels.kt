package com.example.dam_a50804.rickandmorty.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@Serializable
data class CharacterResponse(
    val info: Info,
    val results: List<CharacterDto>
)

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String
)

@Serializable
data class EpisodeResponse(
    val info: Info,
    val results: List<EpisodeDto>
)

@Serializable
data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerialName("episode")
    val episodeCode: String,
    @SerialName("air_date")
    val airDate: String
)

@Serializable
data class LocationResponse(
    val info: Info,
    val results: List<LocationDto>
)

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)
