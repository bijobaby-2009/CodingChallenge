package com.example.albertsonscodingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductListViewModel
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProductListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ProductRepository

    @Mock
    lateinit var productListObserver: Observer<List<Product>>

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProductListViewModel(repository)
        productListObserver = Mockito.mock(Observer::class.java) as Observer<List<Product>>
        viewModel.productList.observeForever(productListObserver)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        viewModel.productList.removeObserver(productListObserver)
    }

    @Test
    fun `fetchProducts should call getLocalProducts from repository and update productList LiveData`() =
        runBlockingTest {
            val dummyProductList = listOf(
                Product(1, "Product 1", "Description 1", 10.0, 0.0, 4.5, 10, "Brand 1", "Category 1", "thumbnail1", emptyList())
            )
            val liveData: LiveData<List<Product>> = MutableLiveData(dummyProductList)
            Mockito.`when`(repository.getLocalProducts()).thenReturn(liveData)

            viewModel = ProductListViewModel(repository)
            viewModel.productList.observeForever(productListObserver)

            viewModel.fetchProducts()

            advanceUntilIdle()

            Mockito.verify(repository).getLocalProducts()

            val latch = CountDownLatch(1)
            var capturedProductList: List<Product>? = null
            viewModel.productList.observeForever { productList ->
                capturedProductList = productList
                latch.countDown()
            }

            Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS))
            Assert.assertEquals(dummyProductList, capturedProductList)
        }
}
