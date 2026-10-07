package com.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    val meta: Meta,
    val categories: List<Category>,
    val items: List<MenuItem>
)

data class Meta(
    val app: String,
    val version: String,
    val tagline: String,
    val currency: String,
    @SerializedName("currency_symbol") val currencySymbol: String,
    @SerializedName("delivery_fee") val deliveryFee: Double,
    @SerializedName("tax_rate_percent") val taxRatePercent: Double,
    @SerializedName("estimated_delivery_time") val estimatedDeliveryTime: String
)