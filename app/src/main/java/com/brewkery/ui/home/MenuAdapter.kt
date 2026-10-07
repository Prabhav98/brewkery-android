package com.brewkery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.brewkery.data.model.MenuItem
import com.brewkery.databinding.ItemMenuBinding

class MenuAdapter(
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.VH>() {

    private var items: List<MenuItem> = emptyList()

    fun submit(list: List<MenuItem>) {
        items = list
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        with(holder.binding) {
            itemImage.load(item.imageUrl) { crossfade(true) }
            itemBadge.text = item.badge ?: ""
            itemBadge.visibility = if (item.badge.isNullOrEmpty()) android.view.View.GONE else android.view.View.VISIBLE
            itemName.text = item.name
            itemRating.text = "${item.rating} (${item.reviewCount})"
            itemPrice.text = "$${String.format("%.2f", item.basePrice)}"
            root.setOnClickListener { onItemClick(item) }
            customizeButton.setOnClickListener { onItemClick(item) }
        }
    }
}