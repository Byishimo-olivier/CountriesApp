package com.example.countriesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.data.model.CountrySummary
import com.example.countriesapp.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val countries: List<CountrySummary>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allCountries = listOf<CountrySummary>()

    init {
        fetchCountries()
    }

    fun fetchCountries() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                allCountries = repository.getAllCountries().sortedBy { it.name.common }
                _uiState.value = HomeUiState.Success(allCountries)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterCountries(query)
    }

    private fun filterCountries(query: String) {
        if (query.isEmpty()) {
            _uiState.value = HomeUiState.Success(allCountries)
        } else {
            val filtered = allCountries.filter {
                it.name.common.contains(query, ignoreCase = true)
            }
            _uiState.value = HomeUiState.Success(filtered)
        }
    }
}
