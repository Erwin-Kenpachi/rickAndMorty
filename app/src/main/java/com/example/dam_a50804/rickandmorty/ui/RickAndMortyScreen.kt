package com.example.dam_a50804.rickandmorty.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dam_a50804.rickandmorty.model.CharacterDto
import com.example.dam_a50804.rickandmorty.model.EpisodeDto
import com.example.dam_a50804.rickandmorty.model.LocationDto
import com.example.dam_a50804.rickandmorty.viewmodel.Category
import com.example.dam_a50804.rickandmorty.viewmodel.RickAndMortyViewModel
import com.example.dam_a50804.rickandmorty.viewmodel.UiState

@Composable
fun RickAndMortyScreen(modifier: Modifier = Modifier, viewModel: RickAndMortyViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        CategorySelection(
            selectedCategory = state.selectedCategory,
            onCategorySelected = { viewModel.updateCategory(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            query = state.searchQuery,
            onQueryChanged = { viewModel.updateSearchQuery(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ResultsCounter(
            totalResults = state.totalResults,
            category = state.selectedCategory
        )
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (val uiState = state.uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Empty -> Text("No results found", style = MaterialTheme.typography.titleMedium)
                is UiState.Error -> Text("Error: ${uiState.message}", color = MaterialTheme.colorScheme.error)
                is UiState.Success -> ItemCard(item = uiState.selectedItem)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        RollButton(
            enabled = state.uiState is UiState.Success,
            onClick = { viewModel.rollRandomItem() }
        )
    }
}

@Composable
fun Header() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Rick & Morty Explorer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Explore and roll characters, episodes, and locations!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CategorySelection(selectedCategory: Category, onCategorySelected: (Category) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Category.values().forEach { category ->
            if (selectedCategory == category) {
                Button(onClick = { onCategorySelected(category) }) {
                    Text(category.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            } else {
                OutlinedButton(onClick = { onCategorySelected(category) }) {
                    Text(category.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search by name...") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ResultsCounter(totalResults: Int, category: Category) {
    val catName = category.name.lowercase().replaceFirstChar { it.uppercase() }
    Text(
        text = "Total $catName in API: $totalResults | Retrieved in current view: 1 item",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ItemCard(item: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (item) {
                is CharacterDto -> {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(8.dp)
                    )
                    Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = "Status: ${item.status}")
                    Text(text = "Species: ${item.species}")
                }
                is EpisodeDto -> {
                    Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Code: ${item.episodeCode}")
                    Text(text = "Air Date: ${item.airDate}")
                }
                is LocationDto -> {
                    Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Type: ${item.type}")
                    Text(text = "Dimension: ${item.dimension}")
                }
            }
        }
    }
}

@Composable
fun RollButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text("Roll Random Item", style = MaterialTheme.typography.titleMedium)
    }
}
