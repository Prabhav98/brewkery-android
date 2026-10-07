package com.brewkery

import com.brewkery.data.model.CartItem
import com.brewkery.data.model.MilkOption
import com.brewkery.data.model.SizeOption
import com.brewkery.utils.PriceCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun `calculate item price with size and milk extras`() {
        val price = PriceCalculator.calculateItemPrice(4.85, 0.65, 0.50)
        assertEquals(6.00, price, 0.001)
    }

    @Test
    fun `calculate item price with zero extras equals base`() {
        val price = PriceCalculator.calculateItemPrice(4.85, 0.0, 0.0)
        assertEquals(4.85, price, 0.001)
    }

    @Test
    fun `calculate subtotal for multiple items`() {
        val items = listOf(
            sampleCartItem(unitPrice = 5.50, qty = 1),
            sampleCartItem(unitPrice = 3.90, qty = 1)
        )
        val subtotal = PriceCalculator.calculateSubtotal(items)
        assertEquals(9.40, subtotal, 0.001)
    }

    @Test
    fun `calculate tax at 8 percent`() {
        val tax = PriceCalculator.calculateTax(9.40)
        assertEquals(0.752, tax, 0.001)
    }

    @Test
    fun `calculate total with delivery and tax`() {
        val total = PriceCalculator.calculateTotal(9.40)
        // 9.40 + 2.50 + 0.752 = 12.652
        assertEquals(12.652, total, 0.001)
    }

    private fun sampleCartItem(unitPrice: Double, qty: Int): CartItem {
        return CartItem(
            cartId = "test_$unitPrice",
            itemId = 1,
            name = "Test Item",
            imageUrl = "",
            selectedSize = SizeOption("s1", "Small", 0.0),
            selectedMilk = MilkOption("m1", "Oat", 0.0),
            selectedSugar = "No sugar",
            unitPrice = unitPrice,
            quantity = qty
        )
    }
}