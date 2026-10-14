package com.example.dam_a50804.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dam_a50804.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    val loadedResults: Int = 0,
    val uiState: UiState = UiState.Loading
) {
    val retrievedCount: Int get() = loadedResults
}

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
        
        _state.update { it.copy(uiState = UiState.Loading, loadedResults = 0) }

        viewModelScope.launch {
            when (currentState.selectedCategory) {
                Category.CHARACTER -> fetchRandomCharacter(query)
                Category.EPISODE -> fetchRandomEpisode(query)
                Category.LOCATION -> fetchRandomLocation(query)

            }
        }
    }

    private suspend fun fetchRandomCharacter(query: String) {
        try {
            val initialResponse = repository.getCharacters(query, 1)
            val total = initialResponse.info.count
            if (total == 0 || initialResponse.results.isEmpty()) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
                return
            }

            val randomIndex = Random.nextInt(total)
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetResponse = if (targetPage == 1) {
                initialResponse
            } else {
                repository.getCharacters(query, targetPage)
            }

            val loadedCount = targetResponse.results.size
            val item = targetResponse.results.getOrNull(itemIndexInPage)

            if (item != null) {
                _state.update {
                    it.copy(
                        totalResults = total,
                        loadedResults = loadedCount,
                        uiState = UiState.Success(item)
                    )
                }
            } else {
                _state.update { it.copy(uiState = UiState.Error("Failed to extract character")) }
            }
        } catch (e: Exception) {
            if ((e is HttpException && e.code() == 404) || e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network or API Error")) }
            }
        }
    }

    private suspend fun fetchRandomEpisode(query: String) {
        try {
            val initialResponse = repository.getEpisodes(query, 1)
            val total = initialResponse.info.count
            if (total == 0 || initialResponse.results.isEmpty()) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
                return
            }

            val randomIndex = Random.nextInt(total)
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetResponse = if (targetPage == 1) {
                initialResponse
            } else {
                repository.getEpisodes(query, targetPage)
            }

            val loadedCount = targetResponse.results.size
            val item = targetResponse.results.getOrNull(itemIndexInPage)

            if (item != null) {
                _state.update {
                    it.copy(
                        totalResults = total,
                        loadedResults = loadedCount,
                        uiState = UiState.Success(item)
                    )
                }
            } else {
                _state.update { it.copy(uiState = UiState.Error("Failed to extract episode")) }
            }
        } catch (e: Exception) {
            if ((e is HttpException && e.code() == 404) || e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network or API Error")) }
            }
        }
    }

    private suspend fun fetchRandomLocation(query: String) {
        try {
            val initialResponse = repository.getLocations(query, 1)
            val total = initialResponse.info.count
            if (total == 0 || initialResponse.results.isEmpty()) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
                return
            }

            val randomIndex = Random.nextInt(total)
            val targetPage = (randomIndex / 20) + 1
            val itemIndexInPage = randomIndex % 20

            val targetResponse = if (targetPage == 1) {
                initialResponse
            } else {
                repository.getLocations(query, targetPage)
            }

            val loadedCount = targetResponse.results.size
            val item = targetResponse.results.getOrNull(itemIndexInPage)

            if (item != null) {
                _state.update {
                    it.copy(
                        totalResults = total,
                        loadedResults = loadedCount,
                        uiState = UiState.Success(item)
                    )
                }
            } else {
                _state.update { it.copy(uiState = UiState.Error("Failed to extract location")) }
            }
        } catch (e: Exception) {
            if ((e is HttpException && e.code() == 404) || e.message?.contains("404") == true) {
                _state.update { it.copy(totalResults = 0, loadedResults = 0, uiState = UiState.Empty) }
            } else {
                _state.update { it.copy(uiState = UiState.Error(e.message ?: "Network or API Error")) }
            }
        }
    }
}
