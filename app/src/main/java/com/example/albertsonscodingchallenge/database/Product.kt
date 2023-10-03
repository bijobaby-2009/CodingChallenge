package com.example.albertsonscodingchallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.albertsonscodingchallenge.util.ImageListConverter

@Entity(tableName = "product")
@TypeConverters(ImageListConverter::class)
data class Product (
    @PrimaryKey
    val id: Int,

    val title: String,

    val description: String,

    val price: Double,

    val discountPercentage: Double,

    val rating: Double,

    val stock: Int,

    val brand: String,

    val category: String,

    val thumbnail: String,

    val images: List<String>

)