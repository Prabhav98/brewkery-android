package com.brewkery.data.local

import androidx.room.*
import com.brewkery.data.model.Category
import com.brewkery.data.model.MenuItem

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<MenuItem>)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category>

    @Query("SELECT * FROM menu_items")
    suspend fun getItems(): List<MenuItem>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getItemById(id: Int): MenuItem?

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Query("DELETE FROM menu_items")
    suspend fun clearItems()
}