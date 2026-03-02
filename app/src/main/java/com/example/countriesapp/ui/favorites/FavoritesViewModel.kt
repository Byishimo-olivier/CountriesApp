package com.example.countriesapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.data.local.FavoriteCountryEntity
import com.example.countriesapp.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val favorites: List<FavoriteCountryEntity>) : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = repository.getFavorites()
        .map { FavoritesUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Loading
        )

    fun removeFavorite(favorite: FavoriteCountryEntity) {
        viewModelScope.launch {
            repository.removeFavorite(favorite)
        }
    }
}
