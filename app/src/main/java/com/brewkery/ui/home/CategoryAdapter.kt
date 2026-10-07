package com.brewkery.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brewkery.R
import com.brewkery.data.model.Category
import com.brewkery.databinding.ItemCategoryBinding
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(
    private val onClick: (String?) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private var categories: List<Category> = emptyList()
    private var selectedId: String? = null // null = All Items

    fun submit(list: List<Category>) {
        categories = list
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = categories.size + 1

    override fun onBindViewHolder(holder: VH, position: Int) {
        val context = holder.itemView.context
        val card = holder.binding.root as MaterialCardView

        if (position == 0) {
            // "All Items"
            holder.binding.categoryIcon.text = ""
            holder.binding.categoryIcon.visibility = android.view.View.GONE
            holder.binding.categoryName.text = "All Items"
            val selected = selectedId == null
            applyStyle(card, holder.binding.categoryName, selected, context)
            card.setOnClickListener {
                selectedId = null
                notifyDataSetChanged()
                onClick(null)
            }
        } else {
            val cat = categories[position - 1]
            holder.binding.categoryIcon.visibility = android.view.View.VISIBLE
            holder.binding.categoryIcon.text = cat.icon
            holder.binding.categoryName.text = cat.name
            val selected = selectedId == cat.id
            applyStyle(card, holder.binding.categoryName, selected, context)
            card.setOnClickListener {
                selectedId = cat.id
                notifyDataSetChanged()
                onClick(cat.id)
            }
        }
    }

    private fun applyStyle(
        card: MaterialCardView,
        nameView: android.widget.TextView,
        selected: Boolean,
        context: android.content.Context
    ) {
        if (selected) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.brand_dark))
            card.strokeWidth = 0
            nameView.setTextColor(Color.WHITE)
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_white))
            card.strokeWidth = 2
            card.strokeColor = ContextCompat.getColor(context, R.color.divider)
            nameView.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        }
    }
}