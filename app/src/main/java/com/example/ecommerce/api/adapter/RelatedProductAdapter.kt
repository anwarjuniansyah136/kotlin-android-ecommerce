package com.example.ecommerce.api.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.ProductDetailActivity
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product

class RelatedProductAdapter(private val onClik:(Product) -> Unit):ListAdapter<Product,RelatedProductAdapter.RelatedProductViewHolder>(RelatedProductCallBack){
    class RelatedProductViewHolder(itemView:View,private val onClik: (Product) -> Unit):RecyclerView.ViewHolder(itemView){
        private val productName:TextView = itemView.findViewById(R.id.related_product_name)
        private val productPrice:TextView = itemView.findViewById(R.id.related_product_price)
        private val thumbnail:ImageView = itemView.findViewById(R.id.related_product_image)
        private var currentProduct:Product?=null
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"

        init {
            itemView.setOnClickListener {
                currentProduct?.let { product ->
                    onClik(product)
                }
            }
        }
        fun bind(product: Product){
            currentProduct = product
            productName.text = product.productName
            productPrice.text = product.productPrice.toString()
            val imageUrl = BASE_URL + product.image
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_related_product,parent,false)
        return RelatedProductViewHolder(view,onClik)
    }

    override fun onBindViewHolder(holder: RelatedProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}
object RelatedProductCallBack:DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

}