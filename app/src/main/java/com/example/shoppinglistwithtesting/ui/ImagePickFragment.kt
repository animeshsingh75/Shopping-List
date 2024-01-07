package com.example.shoppinglistwithtesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppinglistwithtesting.databinding.FragmentImagePickBinding
import com.example.shoppinglistwithtesting.ui.adapter.ImageAdapter
import com.example.shoppinglistwithtesting.ui.viewmodel.ShoppingViewModel
import com.example.shoppinglistwithtesting.utils.Constants.GRID_SHOPPING_IMAGE_SPAN_COUNT
import com.example.shoppinglistwithtesting.utils.Constants.SEARCH_TIME_DELAY
import com.example.shoppinglistwithtesting.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(val imageAdapter: ImageAdapter) : Fragment() {

    private lateinit var binding: FragmentImagePickBinding
    lateinit var viewModel: ShoppingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activityViewModels<ShoppingViewModel>().value
        setUpRecyclerView()
        subscribeToObservers()
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }
        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            imageAdapter.images = listOf()
            viewModel.setCurImageUrl(it ?: "")
        }
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imageAdapter.images = listOf()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, backPressedCallback
        )

    }


    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Resource.Success -> {
                        val urls = result.data?.hits?.map { imageResult ->
                            imageResult.previewURL
                        }
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            binding.root,
                            result.message ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SHOPPING_IMAGE_SPAN_COUNT)
        }
    }
}