package com.example.countriesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.data.model.CountrySummary
import com.example.countriesapp.data.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val countries: List<CountrySummary>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

enum class SortOrder {
    NAME, POPULATION_DESC, POPULATION_ASC
}

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NAME)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var allCountries = listOf<CountrySummary>()

    init {
        fetchCountries()
        setupSearchDebounce()
    }

    fun fetchCountries() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            loadCountries()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadCountries()
            _isRefreshing.value = false
        }
    }

    private suspend fun loadCountries() {
        try {
            allCountries = repository.getAllCountries()
            applyFiltersAndSort()
        } catch (e: Exception) {
            _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    private fun setupSearchDebounce() {
        searchQuery
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { applyFiltersAndSort() }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSortOrderChange(order: SortOrder) {
        _sortOrder.value = order
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        val query = _searchQuery.value
        val order = _sortOrder.value

        var filtered = if (query.isEmpty()) {
            allCountries
        } else {
            allCountries.filter {
                it.name.common.contains(query, ignoreCase = true)
            }
        }

        filtered = when (order) {
            SortOrder.NAME -> filtered.sortedBy { it.name.common }
            SortOrder.POPULATION_DESC -> filtered.sortedByDescending { it.population }
            SortOrder.POPULATION_ASC -> filtered.sortedBy { it.population }
        }

        _uiState.value = HomeUiState.Success(filtered)
    }
}
