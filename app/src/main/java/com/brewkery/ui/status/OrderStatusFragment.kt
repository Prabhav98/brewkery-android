package com.brewkery.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brewkery.databinding.FragmentOrderStatusBinding

class OrderStatusFragment : Fragment() {

    private var _binding: FragmentOrderStatusBinding? = null
    private val binding get() = _binding!!

    private val args: OrderStatusFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ticketIdText.text = "#${args.ticketId}"
        binding.estimatedWaitText.text = args.waitTime
        binding.itemsOrderedText.text = "${args.itemCount} Items"

        binding.backToMenuBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}