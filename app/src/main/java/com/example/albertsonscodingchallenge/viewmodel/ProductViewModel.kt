package com.example.albertsonscodingchallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.api.NetworkState
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.util.NameErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _isProductsAvailable = MutableLiveData<Boolean>(false)
    val isProductsAvailable: LiveData<Boolean>
    get() = _isProductsAvailable

    private val _networkState = MutableLiveData<NetworkState<List<Product>>>()
    val networkState: LiveData<NetworkState<List<Product>>>
    get() = _networkState

    private val _productNameError = MutableLiveData<NameErrorType>(NameErrorType.NONE)
    val productNameError: LiveData<NameErrorType>
    get() = _productNameError

    private var searchStatus: Boolean = false
     val productName = MutableLiveData<String>("")

    fun updateProductStatus(){
        _isProductsAvailable.value =false
    }

    fun setSearchStatus(value:Boolean){
        searchStatus=value
    }

    fun getSearchStatus():Boolean=searchStatus


    private fun hasSpecialCharacters(input: String): Boolean {
        val pattern = "[^a-zA-Z0-9 ]".toRegex()
        return pattern.containsMatchIn(input)
    }


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _networkState.value = NetworkState.Error( throwable.message!!)
    }

    // fetching data from the API
    fun fetchProductList( productNameValue: String) {
        if(productNameValue.isEmpty()){
            _productNameError.value= NameErrorType.EMPTY
        }
        else if (hasSpecialCharacters(productNameValue)){
            _productNameError.value=NameErrorType.SPECIAL
        }
        else{
            viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
                deleteProducts()
                setSearchStatus(true)
                val networkState = repository.getProducts(productNameValue)
                withContext(Dispatchers.Main) {
                    _networkState.value = networkState
                    if (networkState is NetworkState.Success) {
                        _isProductsAvailable.value = networkState.data.isNotEmpty()
                        _productNameError.value=NameErrorType.NONE
                        if(networkState.data.isNotEmpty() )productName.value=""
                    }
                }
            }
        }

    }



    // To clear data stored in the database
    fun deleteProducts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllProducts()
        }
    }
}
