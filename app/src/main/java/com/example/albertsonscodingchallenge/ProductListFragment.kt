package com.example.albertsonscodingchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albertsonscodingchallenge.adapter.ProductListAdapter
import com.example.albertsonscodingchallenge.databinding.FragmentProductListBinding
import com.example.albertsonscodingchallenge.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    var _binding: FragmentProductListBinding?=null
    private val binding get() =_binding!!
    private val viewModel: ProductListViewModel by viewModels ()
    private val args: ProductListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductListAdapter()
        binding.rvProductList.adapter = adapter
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())


        // call to database to fetch product list
        viewModel.fetchProducts()

        //showing product lis in the recyclerview
        viewModel.productList.observe(viewLifecycleOwner) { products ->
            adapter.setList(products)
            adapter.notifyDataSetChanged()
        }

        viewModel.fetchError.observe(viewLifecycleOwner){error->
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}