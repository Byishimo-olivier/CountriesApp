package com.example.countriesapp.data.repository

import com.example.countriesapp.data.local.FavoriteCountryEntity
import com.example.countriesapp.data.local.FavoriteDao
import com.example.countriesapp.data.model.CountryDetail
import com.example.countriesapp.data.model.CountrySummary
import com.example.countriesapp.data.remote.CountryApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepository @Inject constructor(
    private val apiService: CountryApiService,
    private val favoriteDao: FavoriteDao
) {
    suspend fun getAllCountries(): List<CountrySummary> {
        return apiService.getAllCountries()
    }

    suspend fun getCountryDetail(cca2: String): CountryDetail {
        return apiService.getCountryByCode(cca2)
    }

    fun getFavorites(): Flow<List<FavoriteCountryEntity>> {
        return favoriteDao.getAllFavorites()
    }

    suspend fun addFavorite(favorite: FavoriteCountryEntity) {
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(favorite: FavoriteCountryEntity) {
        favoriteDao.deleteFavorite(favorite)
    }

    fun isFavorite(cca2: String): Flow<Boolean> {
        return favoriteDao.isFavorite(cca2)
    }
}
