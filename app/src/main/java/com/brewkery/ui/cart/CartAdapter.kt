package com.brewkery.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brewkery.data.model.CartItem
import com.brewkery.databinding.ItemCartBinding

class CartAdapter(
    private val onIncrement: (String) -> Unit,
    private val onDecrement: (String) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    private var items: List<CartItem> = emptyList()

    fun submit(list: List<CartItem>) {
        items = list
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        with(holder.binding) {
            cartItemName.text = item.name
            cartItemCustomization.text = item.customizationSummary
            cartItemPrice.text = "$${String.format("%.2f", item.totalPrice)}"
            qtyCartText.text = item.quantity.toString()
            incrementCartBtn.setOnClickListener { onIncrement(item.cartId) }
            decrementCartBtn.setOnClickListener { onDecrement(item.cartId) }
        }
    }
}