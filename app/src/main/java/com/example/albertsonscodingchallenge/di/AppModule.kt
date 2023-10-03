package com.example.albertsonscodingchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.AppDatabase
import com.example.albertsonscodingchallenge.database.ProductDao
import com.example.albertsonscodingchallenge.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context):AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase):ProductDao{
        return appDatabase.productDao()
    }



    @Provides
    @Singleton
    fun provideProductService(): ProductService {
        return ProductService.create()
    }

}