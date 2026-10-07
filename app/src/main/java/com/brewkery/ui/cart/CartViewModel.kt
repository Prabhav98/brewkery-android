package com.brewkery.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brewkery.data.model.CartItem
import com.brewkery.utils.PriceCalculator

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _orderTicket = MutableLiveData<String?>(null)
    val orderTicket: LiveData<String?> = _orderTicket

    fun addToCart(item: CartItem) {
        val list = _cartItems.value.orEmpty().toMutableList()
        val existing = list.find { it.cartId == item.cartId }
        if (existing != null) {
            existing.quantity += item.quantity
        } else {
            list.add(item)
        }
        _cartItems.value = list
    }

    fun incrementQuantity(cartId: String) {
        val list = _cartItems.value.orEmpty().toMutableList()
        list.find { it.cartId == cartId }?.let { it.quantity++ }
        _cartItems.value = list
    }

    fun decrementQuantity(cartId: String) {
        val list = _cartItems.value.orEmpty().toMutableList()
        val item = list.find { it.cartId == cartId }
        if (item != null) {
            if (item.quantity > 1) item.quantity--
            else list.remove(item)
        }
        _cartItems.value = list
    }

    fun clearCart() { _cartItems.value = emptyList() }

    fun getSubtotal(): Double = PriceCalculator.calculateSubtotal(_cartItems.value.orEmpty())
    fun getTax(): Double = PriceCalculator.calculateTax(getSubtotal())
    fun getTotal(): Double = PriceCalculator.calculateTotal(getSubtotal())
    fun getItemCount(): Int = _cartItems.value.orEmpty().sumOf { it.quantity }

    fun placeOrder(): String {
        val ticketId = "BK-${(10000..99999).random()}"
        _orderTicket.value = ticketId
        clearCart()
        return ticketId
    }

}