package com.brewkery.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brewkery.R
import com.brewkery.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartAdapter(
            onIncrement = { cartViewModel.incrementQuantity(it) },
            onDecrement = { cartViewModel.decrementQuantity(it) }
        )
        binding.cartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecycler.adapter = adapter

        binding.cartBackBtn.setOnClickListener { findNavController().popBackStack() }
        binding.clearCartBtn.setOnClickListener { cartViewModel.clearCart() }

        binding.placeOrderBtn.setOnClickListener {
            if (cartViewModel.cartItems.value.isNullOrEmpty()) return@setOnClickListener
            val itemCount = cartViewModel.getItemCount()
            val ticketId = cartViewModel.placeOrder()
            val action = CartFragmentDirections.actionCartToStatus(
                ticketId = ticketId,
                itemCount = itemCount,
                waitTime = "20 - 30 minutes"
            )
            findNavController().navigate(action)
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.submit(items)
            binding.emptyCartText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.subtotalText.text = "$${String.format("%.2f", cartViewModel.getSubtotal())}"
            binding.taxText.text = "$${String.format("%.2f", cartViewModel.getTax())}"
            binding.totalText.text = "$${String.format("%.2f", cartViewModel.getTotal())}"
            binding.placeOrderBtn.text = "🔒 Place Order Now • $${String.format("%.2f", cartViewModel.getTotal())}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}