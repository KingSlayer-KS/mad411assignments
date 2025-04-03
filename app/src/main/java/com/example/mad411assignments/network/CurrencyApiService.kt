package com.example.mad411assignments.network

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("currencies/{currency}.json")
    suspend fun getRates(@Path("currency") currency: String): CurrRes
}