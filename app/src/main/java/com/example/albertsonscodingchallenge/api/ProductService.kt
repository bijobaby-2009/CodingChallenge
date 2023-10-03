package com.example.albertsonscodingchallenge.api

import com.example.albertsonscodingchallenge.database.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

interface ProductService {
    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductResponse

    companion object {
        private const val BASE_URL = "https://dummyjson.com/"

        fun create(): ProductService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ProductService::class.java)
        }
    }
}