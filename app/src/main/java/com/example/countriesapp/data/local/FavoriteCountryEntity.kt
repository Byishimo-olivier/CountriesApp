package com.example.countriesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteCountryEntity(
    @PrimaryKey val cca2: String,
    val name: String,
    val flagUrl: String,
    val capital: String?
)
