package com.example.albertsonscodingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.albertsonscodingchallenge.api.NetworkState
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ProductRepository

    @Mock
    lateinit var productListObserver: Observer<List<Product>>

    @Mock
    lateinit var isProductsAvailableObserver: Observer<Boolean>

    private val testDispatcher = TestCoroutineDispatcher()

    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProductViewModel(repository)
        productListObserver = mock(Observer::class.java) as Observer<List<Product>>
        isProductsAvailableObserver = mock(Observer::class.java) as Observer<Boolean>
        viewModel.isProductsAvailable.observeForever(isProductsAvailableObserver)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        viewModel.isProductsAvailable.removeObserver(isProductsAvailableObserver)
    }

    @Test
    fun `updateProductStatus should update isProductsAvailable LiveData to false`() = runBlockingTest {
        viewModel.updateProductStatus()
        advanceUntilIdle()
        assertTrue(viewModel.isProductsAvailable.value == false)
    }

    @Test
    fun `setSearchStatus should update the searchStatus variable`() {
        viewModel.setSearchStatus(true)
        assertTrue(viewModel.getSearchStatus())
        viewModel.setSearchStatus(false)
        assertTrue(!viewModel.getSearchStatus())
    }

    @Test
    fun `fetchProductList with success`() = testScope.runBlockingTest {
        val query = "Test query"
        val product1 = Product(1, "Product 1", "Description 1", 10.0, 0.0, 4.5, 10, "Brand 1", "Category 1", "thumbnail1", emptyList())
        val product2 = Product(2, "Product 2", "Description 2", 20.0, 0.0, 5.0, 20, "Brand 2", "Category 2", "thumbnail2", emptyList())
        val productList = listOf(product1, product2)
        val response = NetworkState.Success(productList)
        `when`(repository.getProducts(query)).thenReturn(response)

        viewModel.fetchProductList(query)

        advanceUntilIdle()

        verify(repository).getProducts(query)

        val latch = CountDownLatch(1)
        var capturedProductList: NetworkState<List<Product>>? = null
        viewModel.networkState.observeForever { productList ->
            capturedProductList = productList
            latch.countDown()
        }

        assertTrue(latch.await(5000, TimeUnit.MILLISECONDS))
        assertEquals(response, capturedProductList)
        assertEquals(true, viewModel.isProductsAvailable.value)
    }

    @Test
    fun `fetchProductList with failure`() = testScope.runBlockingTest {
        val query = "Test query"
        val exception = "Failed to fetch products. Please try again later."
        val response = NetworkState.Error(exception)
        `when`(repository.getProducts(query)).thenThrow(RuntimeException(exception))

        viewModel.fetchProductList(query)

        advanceUntilIdle()

        verify(repository).getProducts(query)

        val latch = CountDownLatch(1)
        var capturedError: NetworkState<List<Product>>? = null
        viewModel.networkState.observeForever { error ->
            capturedError = error
            latch.countDown()
        }

        assertTrue(latch.await(5000, TimeUnit.MILLISECONDS))
        assertEquals(response, capturedError)
        assertEquals(false, viewModel.isProductsAvailable.value)
    }


    @Test
    fun `deleteProducts should call deleteAllProducts from repository`() = testScope.runBlockingTest {
        viewModel.deleteProducts()
        advanceUntilIdle()
        verify(repository).deleteAllProducts()
    }
}
