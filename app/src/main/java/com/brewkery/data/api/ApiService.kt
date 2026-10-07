package com.brewkery.data.api

import com.brewkery.data.model.MenuItem
import com.brewkery.data.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("data.json")
    suspend fun getMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItemById(@Path("id") id: Int): MenuItem
}