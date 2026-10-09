package com.example.dam_a50804.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dam_a50804.rickandmorty.model.Character
import com.example.dam_a50804.rickandmorty.model.Episode
import com.example.dam_a50804.rickandmorty.model.Location
import com.example.dam_a50804.rickandmorty.model.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    private val _state = MutableStateFlow(RickAndMortyState())
    val state: StateFlow<RickAndMortyState> = _state.asStateFlow()

    init {
        // Inicializar com Character e fazer o primeiro Roll
        updateCategory(Category.CHARACTER)
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        rollRandomItem()
    }

    fun updateCategory(category: Category) {
        _state.update { it.copy(selectedCategory = category, searchQuery = "") }
        rollRandomItem()
    }

    fun rollRandomItem() {
        val currentState = _state.value
        val query = currentState.searchQuery.lowercase()

        val filteredItems = when (currentState.selectedCategory) {
            Category.CHARACTER -> MockData.characters.filter { it.name.lowercase().contains(query) }
            Category.EPISODE -> MockData.episodes.filter { it.name.lowercase().contains(query) }
            Category.LOCATION -> MockData.locations.filter { it.name.lowercase().contains(query) }
        }

        val totalResults = filteredItems.size

        if (totalResults == 0) {
            _state.update {
                it.copy(
                    totalResults = 0,
                    uiState = UiState.Empty
                )
            }
            return
        }

        // Evitar repetição consecutiva
        val currentItem = (currentState.uiState as? UiState.Success)?.selectedItem
        val candidates = if (totalResults > 1 && currentItem != null) {
            filteredItems.filter { it != currentItem }
        } else {
            filteredItems
        }

        val newItem = candidates.random()

        _state.update {
            it.copy(
                totalResults = totalResults,
                uiState = UiState.Success(newItem)
            )
        }
    }
}
