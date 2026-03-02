package com.example.countriesapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteCountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteCountryEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteCountryEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE cca2 = :cca2)")
    fun isFavorite(cca2: String): Flow<Boolean>
}
