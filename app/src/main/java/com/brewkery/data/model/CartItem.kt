package com.brewkery.data.model

data class CartItem(
    val cartId: String, // unique per customization combo
    val itemId: Int,
    val name: String,
    val imageUrl: String,
    val selectedSize: SizeOption,
    val selectedMilk: MilkOption,
    val selectedSugar: String,
    val unitPrice: Double,
    var quantity: Int
) {
    val totalPrice: Double get() = unitPrice * quantity
    val customizationSummary: String get() = "${selectedSize.label} • ${selectedMilk.name}"
}