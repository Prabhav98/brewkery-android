package com.brewkery.utils

import com.brewkery.data.model.CartItem

object PriceCalculator {
    const val DELIVERY_FEE = 2.50
    const val TAX_RATE = 0.08

    fun calculateItemPrice(basePrice: Double, sizeExtra: Double, milkExtra: Double): Double {
        return basePrice + sizeExtra + milkExtra
    }

    fun calculateSubtotal(cartItems: List<CartItem>): Double {
        return cartItems.sumOf { it.totalPrice }
    }

    fun calculateTax(subtotal: Double): Double {
        return subtotal * TAX_RATE
    }

    fun calculateTotal(subtotal: Double): Double {
        return subtotal + DELIVERY_FEE + calculateTax(subtotal)
    }
}