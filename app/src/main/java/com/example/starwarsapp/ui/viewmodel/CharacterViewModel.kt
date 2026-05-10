package com.example.starwarsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsapp.data.repository.CharacterRepository
import com.example.starwarsapp.ui.state.CharacterUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterRepository = CharacterRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Loading)
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private var searchJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = CharacterUiState.Loading
            try {
                val characters = repository.getCharacters()
                _uiState.value = if (characters.isEmpty()) {
                    CharacterUiState.Error("No se encontraron personajes.")
                } else {
                    CharacterUiState.Success(characters)
                }
            } catch (e: Exception) {
                _uiState.value = CharacterUiState.Error(
                    "Error de conexión: ${e.localizedMessage ?: "Revisa tu internet."}"
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L) // Esperar 500ms antes de buscar
            _uiState.value = CharacterUiState.Loading
            try {
                val characters = repository.searchCharacters(query)
                _uiState.value = if (characters.isEmpty()) {
                    CharacterUiState.Error("No se encontraron personajes para \"$query\".")
                } else {
                    CharacterUiState.Success(characters)
                }
            } catch (e: Exception) {
                _uiState.value = CharacterUiState.Error(
                    "Error de conexión: ${e.localizedMessage ?: "Revisa tu internet."}"
                )
            }
        }
    }
}
