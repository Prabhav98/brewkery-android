package com.brewkery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey val id: Int,
    @SerializedName("category_id") val categoryId: String,
    val name: String,
    val tagline: String,
    val description: String,
    @SerializedName("base_price") val basePrice: Double,
    val rating: Double,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("prep_time") val prepTime: String,
    val calories: Int,
    @SerializedName("image_url") val imageUrl: String,
    val badge: String?,
    val ingredients: List<String>,
    val customizations: Customizations
)

data class Customizations(
    val sizes: List<SizeOption>,
    @SerializedName("sugar_levels") val sugarLevels: List<String>,
    @SerializedName("milk_options") val milkOptions: List<MilkOption>
)

data class SizeOption(
    val id: String,
    val label: String,
    @SerializedName("extra_price") val extraPrice: Double
)

data class MilkOption(
    val id: String,
    val name: String,
    @SerializedName("extra_price") val extraPrice: Double
)