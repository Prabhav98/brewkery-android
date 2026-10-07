package com.brewkery.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.brewkery.R
import com.brewkery.data.model.CartItem
import com.brewkery.data.model.MilkOption
import com.brewkery.data.model.SizeOption
import com.brewkery.databinding.FragmentDetailBinding
import com.brewkery.ui.cart.CartViewModel
import com.brewkery.utils.Resource
import com.google.android.material.chip.Chip

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadItem(args.itemId)
        observe()

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.incrementBtn.setOnClickListener { viewModel.incrementQty() }
        binding.decrementBtn.setOnClickListener { viewModel.decrementQty() }

        binding.addToCartBtn.setOnClickListener { addToCart() }
    }

    private fun observe() {
        viewModel.itemState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.detailProgress.visibility = View.VISIBLE
                    binding.detailContent.visibility = View.GONE
                    binding.detailError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.detailProgress.visibility = View.GONE
                    binding.detailContent.visibility = View.VISIBLE
                    bindItem(state.data)
                }
                is Resource.Error -> {
                    binding.detailProgress.visibility = View.GONE
                    binding.detailError.visibility = View.VISIBLE
                    binding.detailError.text = state.message
                }
            }
        }

        viewModel.unitPrice.observe(viewLifecycleOwner) { price ->
            binding.detailPrice.text = "$${String.format("%.2f", price)}"
            val qty = viewModel.quantity.value ?: 1
            binding.addToCartBtn.text = "Add to Cart • $${String.format("%.2f", price * qty)}"
        }

        viewModel.quantity.observe(viewLifecycleOwner) { qty ->
            binding.qtyText.text = qty.toString()
            val price = viewModel.unitPrice.value ?: 0.0
            binding.addToCartBtn.text = "Add to Cart • $${String.format("%.2f", price * qty)}"
        }

        viewModel.selectedSize.observe(viewLifecycleOwner) { refreshSizes() }
        viewModel.selectedMilk.observe(viewLifecycleOwner) { refreshMilk() }
        viewModel.selectedSugar.observe(viewLifecycleOwner) { refreshSugar() }
    }

    private fun bindItem(item: com.brewkery.data.model.MenuItem) {
        binding.heroImage.load(item.imageUrl) { crossfade(true) }
        binding.detailName.text = item.name
        binding.detailDescription.text = item.description

        binding.ingredientsChipGroup.removeAllViews()
        item.ingredients.forEach { ing ->
            val chip = Chip(requireContext())
            chip.text = ing
            chip.isClickable = false
            binding.ingredientsChipGroup.addView(chip)
        }

        buildSizes(item.customizations.sizes)
        buildMilk(item.customizations.milkOptions)
        buildSugar(item.customizations.sugarLevels)
    }

    // Put this inside DetailFragment.kt

    private fun buildSizes(sizes: List<SizeOption>) {
        binding.sizesContainer.removeAllViews()
        sizes.forEach { size ->
            val card = com.google.android.material.card.MaterialCardView(requireContext()).apply {
                radius = 16f
                cardElevation = 0f
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                lp.marginEnd = 8
                layoutParams = lp
                strokeWidth = 2
            }

            val content = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 16, 12, 16)
                gravity = android.view.Gravity.CENTER
            }

            val label = TextView(requireContext()).apply {
                text = size.label
                setTextColor(android.graphics.Color.BLACK)
                textSize = 12f
                maxLines = 1
                gravity = android.view.Gravity.CENTER
            }
            val price = TextView(requireContext()).apply {
                text = "+$${String.format("%.2f", size.extraPrice)}"
                setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_secondary))
                textSize = 11f
                gravity = android.view.Gravity.CENTER
            }

            content.addView(label)
            content.addView(price)
            card.addView(content)
            card.setOnClickListener { viewModel.selectSize(size) }
            card.tag = size.id
            binding.sizesContainer.addView(card)
        }
        refreshSizes()
    }

    private fun refreshSizes() {
        val selected = viewModel.selectedSize.value?.id
        for (i in 0 until binding.sizesContainer.childCount) {
            val card = binding.sizesContainer.getChildAt(i) as com.google.android.material.card.MaterialCardView
            val isSelected = card.tag == selected

            // Figma selection style: Orange border + Light orange background
            card.strokeColor = androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_orange else R.color.divider)
            card.setCardBackgroundColor(androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_orange_light else R.color.card_white))
        }
    }

    private fun buildMilk(options: List<MilkOption>) {
        binding.milkContainer.removeAllViews()
        options.forEach { milk ->
            // Wrap in MaterialCardView for borders
            val card = com.google.android.material.card.MaterialCardView(requireContext()).apply {
                radius = 24f
                cardElevation = 0f
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.bottomMargin = 16 // spacing between rows
                layoutParams = lp
                strokeWidth = 2
            }

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(32, 24, 32, 24)
            }

            val name = TextView(requireContext()).apply {
                text = milk.name
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setTextColor(android.graphics.Color.BLACK)
            }
            val price = TextView(requireContext()).apply {
                text = "+$${String.format("%.2f", milk.extraPrice)}"
                setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_secondary))
            }

            row.addView(name); row.addView(price)
            card.addView(row)
            card.setOnClickListener { viewModel.selectMilk(milk) }
            card.tag = milk.id
            binding.milkContainer.addView(card)
        }
        refreshMilk()
    }

    private fun refreshMilk() {
        val selected = viewModel.selectedMilk.value?.id
        for (i in 0 until binding.milkContainer.childCount) {
            val card = binding.milkContainer.getChildAt(i) as com.google.android.material.card.MaterialCardView
            val isSelected = card.tag == selected

            // Orange border + Light orange background when selected
            card.strokeColor = androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_orange else R.color.divider)
            card.setCardBackgroundColor(androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_orange_light else R.color.card_white))
        }
    }

    private fun buildSugar(levels: List<String>) {
        binding.sugarContainer.removeAllViews()
        // Make sugar container scroll horizontally if needed
        levels.forEach { level ->
            val card = com.google.android.material.card.MaterialCardView(requireContext()).apply {
                radius = 20f
                cardElevation = 0f
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.marginEnd = 8
                layoutParams = lp
                strokeWidth = 2
            }

            val tv = TextView(requireContext()).apply {
                text = level
                setPadding(20, 14, 20, 14)
                setTextColor(android.graphics.Color.BLACK)
                textSize = 12f
                maxLines = 1
                // Prevent ugly wrapping
                isSingleLine = true
            }

            card.addView(tv)
            card.setOnClickListener { viewModel.selectSugar(level) }
            card.tag = level
            binding.sugarContainer.addView(card)
        }
        refreshSugar()
    }

    private fun refreshSugar() {
        val selected = viewModel.selectedSugar.value
        for (i in 0 until binding.sugarContainer.childCount) {
            val card = binding.sugarContainer.getChildAt(i) as com.google.android.material.card.MaterialCardView
            val tv = card.getChildAt(0) as TextView
            val isSelected = card.tag == selected

            // Dark Brown background when selected (from Figma)
            card.strokeColor = androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_dark else R.color.divider)
            card.setCardBackgroundColor(androidx.core.content.ContextCompat.getColor(requireContext(),
                if (isSelected) R.color.brand_dark else R.color.card_white))
            tv.setTextColor(if (isSelected) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
        }
    }

    private fun addToCart() {
        val item = viewModel.getCurrentItem() ?: return
        val size = viewModel.selectedSize.value ?: return
        val milk = viewModel.selectedMilk.value ?: return
        val sugar = viewModel.selectedSugar.value ?: return
        val qty = viewModel.quantity.value ?: 1
        val unitPrice = viewModel.unitPrice.value ?: item.basePrice

        val cartId = "${item.id}_${size.id}_${milk.id}_${sugar.hashCode()}"
        val cartItem = CartItem(
            cartId = cartId,
            itemId = item.id,
            name = item.name,
            imageUrl = item.imageUrl,
            selectedSize = size,
            selectedMilk = milk,
            selectedSugar = sugar,
            unitPrice = unitPrice,
            quantity = qty
        )
        cartViewModel.addToCart(cartItem)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}