package com.brewkery.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brewkery.data.local.AppDatabase
import com.brewkery.data.model.Category
import com.brewkery.data.model.MenuItem
import com.brewkery.data.model.Meta
import com.brewkery.data.repository.BrewkeryRepository
import com.brewkery.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BrewkeryRepository(AppDatabase.getInstance(application).menuDao())

    private val _menuState = MutableLiveData<Resource<Unit>>()
    val menuState: LiveData<Resource<Unit>> = _menuState

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _items = MutableLiveData<List<MenuItem>>()
    val items: LiveData<List<MenuItem>> = _items

    private val _meta = MutableLiveData<Meta>()
    val meta: LiveData<Meta> = _meta

    // Full unfiltered list kept in memory
    private var allItems: List<MenuItem> = emptyList()

    // null  = All Items
    // "cat_xxx" = specific category
    private var selectedCategoryId: String? = null
    private var searchQuery: String = ""

    fun loadMenu() {
        _menuState.value = Resource.Loading
        viewModelScope.launch {
            try {
                // 1. Show cache immediately if available
                val cachedItems = repo.getCachedItems()
                val cachedCats = repo.getCachedCategories()
                if (cachedItems.isNotEmpty()) {
                    allItems = cachedItems
                    _categories.value = cachedCats
                    selectedCategoryId = null
                    searchQuery = ""
                    applyFilters()
                }

                // 2. Fresh fetch from API
                val response = repo.fetchMenuFromApi()
                allItems = response.items
                _meta.value = response.meta
                _categories.value = response.categories

                // Reset filters after fresh load so user always sees All Items
                selectedCategoryId = null
                searchQuery = ""
                applyFilters()

                _menuState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                if (allItems.isEmpty()) {
                    _menuState.value = Resource.Error(e.message ?: "Failed to load menu")
                } else {
                    // Cache was already shown — treat as soft success
                    _menuState.value = Resource.Success(Unit)
                }
            }
        }
    }

    fun filterByCategory(categoryId: String?) {
        // null or blank → All Items
        selectedCategoryId = categoryId?.takeIf { it.isNotBlank() }
        applyFilters()
    }

    fun search(query: String) {
        searchQuery = query.trim()
        applyFilters()
    }

    private fun applyFilters() {
        var list = allItems

        // Category filter (skip when null = All Items)
        selectedCategoryId?.let { catId ->
            list = list.filter { it.categoryId == catId }
        }

        // Search filter
        if (searchQuery.isNotEmpty()) {
            list = list.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.tagline.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }
        }

        Log.d(
            "BrewkeryFilter",
            "catId=$selectedCategoryId | search='$searchQuery' | all=${allItems.size} | result=${list.size}"
        )

        _items.value = list
    }
}