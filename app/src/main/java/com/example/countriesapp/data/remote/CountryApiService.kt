package com.example.countriesapp.data.remote

import com.example.countriesapp.data.model.CountryDetail
import com.example.countriesapp.data.model.CountrySummary
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryApiService {
    @GET("all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,flags,population,cca2"
    ): List<CountrySummary>

    @GET("alpha/{cca2}")
    suspend fun getCountryByCode(
        @Path("cca2") cca2: String,
        @Query("fields") fields: String = "name,flags,population,cca2,capital,region,subregion,area,timezones"
    ): CountryDetail
}
