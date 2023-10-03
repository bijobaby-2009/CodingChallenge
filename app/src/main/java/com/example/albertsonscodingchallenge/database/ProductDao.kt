package com.example.albertsonscodingchallenge.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface  ProductDao {

    @Query("SELECT * FROM product")
    fun getAllProducts(): LiveData<List<Product>>

    @Upsert
    suspend fun insertProducts(products: List<Product>)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()

}