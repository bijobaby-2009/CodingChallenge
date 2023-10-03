package com.example.albertsonscodingchallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
    get() = _productList

    private val _fetchError =MutableLiveData<String>()
    val fetchError:LiveData<String>
    get() = _fetchError

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _fetchError.value = throwable.message!!
    }

    // fetching products data from the database
    fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            val response  = repository.getLocalProducts()
            withContext(Dispatchers.Main) {
                response.observeForever { productList->
                    _productList.value=productList
                }
            }


        }
    }
}