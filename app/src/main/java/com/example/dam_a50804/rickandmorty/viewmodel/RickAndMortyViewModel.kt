package com.example.dam_a50804.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_a50804.rickandmorty.model.CharacterDto
import com.example.dam_a50804.rickandmorty.model.EpisodeDto
import com.example.dam_a50804.rickandmorty.model.LocationDto
import com.example.dam_a50804.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Category {
    CHARACTER, EPISODE, LOCATION
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val selectedItem: Any) : UiState()
    object Empty : UiState()
    data class Error(val message: String) : UiState()
}

data class RickAndMortyState(
    val selectedCategory: Category = Category.CHARACTER,
    val searchQuery: String = "",
    val totalResults: Int = 0,
    val uiState: UiState = UiState.Loading
)

class RickAndMortyViewModel : ViewModel() {

    private val repository = RickAndMortyRepository()

    private val _state = MutableStateFlow(RickAndMortyState())
    val state: StateFlow<RickAndMortyState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        rollRandomItem()
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            rollRandomItem()
        }
    }

    fun updateCategory(category: Category) {
        _state.update { it.copy(selectedCategory = category, searchQuery = "") }
        rollRandomItem()
    }

    fun rollRandomItem() {
        val currentState = _state.value
        val query = currentState.searchQuery
        
        _state.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch {
            when (currentState.selectedCategory) {
                Category.CHARACTER -> fetchRandomCharacter(query)
                Category.EPISODE -> fetchRandomEpisode(query)
                Category.LOCATION -> fetchRandomLocation(query)
            }
        }
    }

    private suspend fun fetchRandomCharacter(query: String) {
        val initialRes = repository.getCharacters(query, 1)
        initialRes.onSuccess { response ->
            val total = response.info.count
            if (total == 0) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
                return@onSuccess
            }
            
            // Random index from 0 to total - 1
            val randomIndex = Random.nextInt(total)
            // 20 items per page
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetRes = if (targetPage == 1) initialRes else repository.getCharacters(query, targetPage)
            
            targetRes.onSuccess { targetResponse ->
                val item = targetResponse.results.getOrNull(itemIndexInPage)
                if (item != null) {
                    _state.update { it.copy(totalResults = total, uiState = UiState.Success(item)) }
                } else {
                    _state.update { it.copy(uiState = UiState.Error("Failed to extract character")) }
                }
            }.onFailure { e ->
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Unknown error")) }
            }
        }.onFailure { e ->
            if (e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network Error")) }
            }
        }
    }

    private suspend fun fetchRandomEpisode(query: String) {
        val initialRes = repository.getEpisodes(query, 1)
        initialRes.onSuccess { response ->
            val total = response.info.count
            if (total == 0) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
                return@onSuccess
            }
            
            val randomIndex = Random.nextInt(total)
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetRes = if (targetPage == 1) initialRes else repository.getEpisodes(query, targetPage)
            
            targetRes.onSuccess { targetResponse ->
                val item = targetResponse.results.getOrNull(itemIndexInPage)
                if (item != null) {
                    _state.update { it.copy(totalResults = total, uiState = UiState.Success(item)) }
                } else {
                    _state.update { it.copy(uiState = UiState.Error("Failed to extract episode")) }
                }
            }.onFailure { e ->
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Unknown error")) }
            }
        }.onFailure { e ->
            if (e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network Error")) }
            }
        }
    }

    private suspend fun fetchRandomLocation(query: String) {
        val initialRes = repository.getLocations(query, 1)
        initialRes.onSuccess { response ->
            val total = response.info.count
            if (total == 0) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
                return@onSuccess
            }
            
            val randomIndex = Random.nextInt(total)
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetRes = if (targetPage == 1) initialRes else repository.getLocations(query, targetPage)
            
            targetRes.onSuccess { targetResponse ->
                val item = targetResponse.results.getOrNull(itemIndexInPage)
                if (item != null) {
                    _state.update { it.copy(totalResults = total, uiState = UiState.Success(item)) }
                } else {
                    _state.update { it.copy(uiState = UiState.Error("Failed to extract location")) }
                }
            }.onFailure { e ->
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Unknown error")) }
            }
        }.onFailure { e ->
            if (e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network Error")) }
            }
        }
    }
}
