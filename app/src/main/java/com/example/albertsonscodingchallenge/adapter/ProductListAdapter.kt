package com.example.albertsonscodingchallenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.databinding.ItemProductBinding
import com.example.albertsonscodingchallenge.util.toFormattedPrice

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    var productList= ArrayList<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setList(list: List<Product>) {
        productList.clear()
        productList.addAll(list)

    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product=product
            binding.txtProductPrice.text = product.price.toFormattedPrice()
            Glide.with(binding.productImageView)
                .load(product.images[0])
                .centerCrop()
                .into(binding.productImageView)
            binding.executePendingBindings()
        }
    }
}