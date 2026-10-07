package com.brewkery.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brewkery.data.local.AppDatabase
import com.brewkery.data.model.MenuItem
import com.brewkery.data.model.MilkOption
import com.brewkery.data.model.SizeOption
import com.brewkery.data.repository.BrewkeryRepository
import com.brewkery.utils.PriceCalculator
import com.brewkery.utils.Resource
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = BrewkeryRepository(AppDatabase.getInstance(application).menuDao())

    private val _itemState = MutableLiveData<Resource<MenuItem>>()
    val itemState: LiveData<Resource<MenuItem>> = _itemState

    private val _selectedSize = MutableLiveData<SizeOption>()
    val selectedSize: LiveData<SizeOption> = _selectedSize

    private val _selectedMilk = MutableLiveData<MilkOption>()
    val selectedMilk: LiveData<MilkOption> = _selectedMilk

    private val _selectedSugar = MutableLiveData<String>()
    val selectedSugar: LiveData<String> = _selectedSugar

    private val _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    private val _unitPrice = MutableLiveData(0.0)
    val unitPrice: LiveData<Double> = _unitPrice

    private var currentItem: MenuItem? = null

    fun loadItem(id: Int) {
        _itemState.value = Resource.Loading
        viewModelScope.launch {
            try {
                val item = repo.fetchItemDetail(id)
                currentItem = item
                // Set defaults
                _selectedSize.value = item.customizations.sizes.first()
                _selectedMilk.value = item.customizations.milkOptions.first()
                _selectedSugar.value = item.customizations.sugarLevels.first()
                recalculate()
                _itemState.value = Resource.Success(item)
            } catch (e: Exception) {
                // Fallback to cache
                val cached = repo.getCachedItemById(id)
                if (cached != null) {
                    currentItem = cached
                    _selectedSize.value = cached.customizations.sizes.first()
                    _selectedMilk.value = cached.customizations.milkOptions.first()
                    _selectedSugar.value = cached.customizations.sugarLevels.first()
                    recalculate()
                    _itemState.value = Resource.Success(cached)
                } else {
                    _itemState.value = Resource.Error(e.message ?: "Failed to load item")
                }
            }
        }
    }

    fun selectSize(size: SizeOption) {
        _selectedSize.value = size
        recalculate()
    }

    fun selectMilk(milk: MilkOption) {
        _selectedMilk.value = milk
        recalculate()
    }

    fun selectSugar(sugar: String) {
        _selectedSugar.value = sugar
    }

    fun incrementQty() { _quantity.value = (_quantity.value ?: 1) + 1 }
    fun decrementQty() {
        val current = _quantity.value ?: 1
        if (current > 1) _quantity.value = current - 1
    }

    private fun recalculate() {
        val item = currentItem ?: return
        val size = _selectedSize.value ?: return
        val milk = _selectedMilk.value ?: return
        _unitPrice.value = PriceCalculator.calculateItemPrice(item.basePrice, size.extraPrice, milk.extraPrice)
    }

    fun getCurrentItem() = currentItem
}