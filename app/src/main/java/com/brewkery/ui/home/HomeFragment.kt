package com.brewkery.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brewkery.R
import com.brewkery.databinding.FragmentHomeBinding
import com.brewkery.ui.cart.CartViewModel
import com.brewkery.utils.Resource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        setupSearch()
        observe()
        viewModel.loadMenu()

        binding.cartButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_cart)
        }
        binding.bottomCartBar.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_cart)
        }
    }

    private fun setupRecyclers() {
        // Categories
        categoryAdapter = CategoryAdapter { categoryId ->
            // categoryId is null when "All Items" is tapped
            viewModel.filterByCategory(categoryId)
        }
        binding.categoryRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
            // Prevent the parent ScrollView from stealing horizontal swipes
            isNestedScrollingEnabled = false
        }

        // Menu items
        menuAdapter = MenuAdapter { item ->
            val action = HomeFragmentDirections.actionHomeToDetail(item.id)
            findNavController().navigate(action)
        }
        binding.itemsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuAdapter
            // Critical: false so the outer ScrollView handles vertical scroll
            isNestedScrollingEnabled = false
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observe() {
        viewModel.menuState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = state.message
                }
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { cats ->
            categoryAdapter.submit(cats)
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            menuAdapter.submit(items)
        }

        viewModel.meta.observe(viewLifecycleOwner) { meta ->
            binding.deliveryTimeText.text = "Delivery in ${meta.estimatedDeliveryTime}"
            binding.feeText.text = "$${meta.deliveryFee} flat fee"
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            val count = items.sumOf { it.quantity }
            if (count > 0) {
                binding.cartBadge.visibility = View.VISIBLE
                binding.cartBadge.text = count.toString()
                binding.cartItemCountBadge.text = count.toString()
                binding.cartTotalText.text = "$${String.format("%.2f", cartViewModel.getSubtotal())}"
                binding.bottomCartBar.visibility = View.VISIBLE
            } else {
                binding.cartBadge.visibility = View.GONE
                binding.bottomCartBar.visibility = View.GONE
            }
        }

        cartViewModel.orderTicket.observe(viewLifecycleOwner) { ticket ->
            if (ticket != null) {
                binding.activeOrderBanner.visibility = View.VISIBLE
                binding.activeOrderTicket.text = "#$ticket"
            } else {
                binding.activeOrderBanner.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}