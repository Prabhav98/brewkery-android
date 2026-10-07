package com.brewkery.data.repository

import com.brewkery.data.api.RetrofitClient
import com.brewkery.data.local.MenuDao
import com.brewkery.data.model.Category
import com.brewkery.data.model.MenuItem
import com.brewkery.data.model.MenuResponse

class BrewkeryRepository(private val menuDao: MenuDao) {

    private val api = RetrofitClient.apiService

    suspend fun fetchMenuFromApi(): MenuResponse {
        val response = api.getMenu()
        menuDao.clearCategories()
        menuDao.clearItems()
        menuDao.insertCategories(response.categories)
        menuDao.insertItems(response.items)
        return response
    }

    suspend fun getCachedCategories(): List<Category> = menuDao.getCategories()
    suspend fun getCachedItems(): List<MenuItem> = menuDao.getItems()
    suspend fun getCachedItemById(id: Int): MenuItem? = menuDao.getItemById(id)

    suspend fun fetchItemDetail(id: Int): MenuItem = api.getItemById(id)
}