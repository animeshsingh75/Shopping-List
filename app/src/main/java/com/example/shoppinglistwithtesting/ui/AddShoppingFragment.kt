package com.example.shoppinglistwithtesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.shoppinglistwithtesting.databinding.FragmentAddShoppingItemBinding
import com.example.shoppinglistwithtesting.ui.viewmodel.ShoppingViewModel
import com.example.shoppinglistwithtesting.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingFragment @Inject constructor(
    val glide: RequestManager
) : Fragment() {
    private lateinit var binding: FragmentAddShoppingItemBinding
    lateinit var viewModel: ShoppingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = activityViewModels<ShoppingViewModel>().value
        binding = FragmentAddShoppingItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        binding.btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.etShoppingItemAmount.text.toString(),
                binding.etShoppingItemPrice.text.toString()
            )
        }
        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingFragmentDirections.actionAddShoppingFragmentToImagePickFragment()
            )
        }

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    private fun subscribeToObservers() {
        viewModel.currImageUrl.observe(viewLifecycleOwner) {
            glide.load(it).into(binding.ivShoppingImage)
        }

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Resource.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }

                    is Resource.Loading -> {
                    }
                }

            }
        }
    }
}