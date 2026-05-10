package com.example.starwarsapp.ui.state

import com.example.starwarsapp.data.model.Character

sealed class CharacterUiState {
    object Loading : CharacterUiState()
    data class Success(val characters: List<Character>) : CharacterUiState()
    data class Error(val message: String) : CharacterUiState()
}
