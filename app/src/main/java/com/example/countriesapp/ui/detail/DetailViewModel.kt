package com.example.countriesapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.data.local.FavoriteCountryEntity
import com.example.countriesapp.data.model.CountryDetail
import com.example.countriesapp.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val country: CountryDetail, val isFavorite: Boolean) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CountryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cca2: String = checkNotNull(savedStateHandle["cca2"])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        fetchCountryDetail()
    }

    fun fetchCountryDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val country = repository.getCountryDetail(cca2)
                repository.isFavorite(cca2).collect { isFav ->
                    _uiState.value = DetailUiState.Success(country, isFav)
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun toggleFavorite(country: CountryDetail, isFavorite: Boolean) {
        viewModelScope.launch {
            val entity = FavoriteCountryEntity(
                cca2 = country.cca2,
                name = country.name.common,
                flagUrl = country.flags.png,
                capital = country.capital?.firstOrNull()
            )
            if (isFavorite) {
                repository.removeFavorite(entity)
            } else {
                repository.addFavorite(entity)
            }
        }
    }
}
