package com.example.albertsonscodingchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.albertsonscodingchallenge.databinding.FragmentProductNameBinding
import com.example.albertsonscodingchallenge.api.NetworkState
import com.example.albertsonscodingchallenge.util.NameErrorType
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductNameFragment : Fragment() {
    var _binding: FragmentProductNameBinding? =null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProductNameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.isProductsAvailable.observe(viewLifecycleOwner) {
            if(it){
                // navigate to product listing screen
                val productName = binding.tvProductName.text.toString()
                val action = ProductNameFragmentDirections.actionProductNameFragmentToProductListFragment(productName)
                findNavController().navigate(action)

                viewModel.setSearchStatus(false)
                viewModel.updateProductStatus()
            }
            else{
                // show toast if product is not available
                if(viewModel.getSearchStatus()){
                    Toast.makeText(context,getString(R.string.products_not_available),Toast.LENGTH_SHORT).show()
                    viewModel.setSearchStatus(false)
                }

            }
        }

        viewModel.networkState.observe(viewLifecycleOwner){state->
            if (state is NetworkState.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

        // showing errors related to input
        viewModel.productNameError.observe(viewLifecycleOwner){state ->
           when(state){
               NameErrorType.EMPTY->  Toast.makeText(context,getString(R.string.enter_product_name),Toast.LENGTH_SHORT).show()
               NameErrorType.SPECIAL->Toast.makeText(context,getString(R.string.product_special_characters),Toast.LENGTH_SHORT).show()
               else -> {}
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}