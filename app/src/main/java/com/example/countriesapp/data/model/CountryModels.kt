package com.example.countriesapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CountryName(
    val common: String,
    val official: String? = null
)

@Serializable
data class CountryFlags(
    val png: String,
    val svg: String? = null,
    val alt: String? = null
)

@Serializable
data class CountrySummary(
    val name: CountryName,
    val flags: CountryFlags,
    val population: Long,
    val cca2: String
)

@Serializable
data class CountryDetail(
    val name: CountryName,
    val flags: CountryFlags,
    val population: Long,
    val cca2: String,
    val capital: List<String>? = null,
    val region: String,
    val subregion: String? = null,
    val area: Double,
    val timezones: List<String>
)
