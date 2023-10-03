package com.example.albertsonscodingchallenge.di

import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductListViewModel
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideProductViewModel(repository: ProductRepository): ProductViewModel {
        return ProductViewModel(repository)
    }

    @Provides
    fun provideProductListViewModel(repository: ProductRepository): ProductListViewModel {
        return ProductListViewModel(repository)
    }
}
