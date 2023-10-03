package com.example.albertsonscodingchallenge.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.albertsonscodingchallenge.ProductNameFragment
import com.example.albertsonscodingchallenge.R
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.database.ProductDao
import com.example.albertsonscodingchallenge.api.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepository @Inject constructor(@ApplicationContext private val context: Context, private val productService: ProductService, private val productDao: ProductDao) {

    suspend fun getProducts(query: String): NetworkState<List<Product>> {

        return try {
            val response = productService.searchProducts(query) // api call
            productDao.insertProducts(response.products) // saving data to database
            NetworkState.Success(response.products)
        } catch (e: HttpException) {
            NetworkState.Error(context.getString(R.string.http_server_exception))
        } catch (e: IOException) {
            NetworkState.Error(context.getString(R.string.Io_Exception_Error))
        } catch (e: Exception) {
            NetworkState.Error(context.getString(R.string.normal_Exception_error))
        }
    }

    fun getLocalProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    suspend fun deleteAllProducts(){
        return productDao.deleteAllProducts()
    }
}