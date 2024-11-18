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
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product

class NewProductAdapter(private val onClick:(Product) -> Unit):ListAdapter<Product,NewProductAdapter.NewProductViewHolder>(NewProductCallBack) {
    class NewProductViewHolder(itemView:View,private val onClick: (Product) -> Unit):RecyclerView.ViewHolder(itemView){
        private val productName:TextView = itemView.findViewById(R.id.title)
        private val productPrice:TextView = itemView.findViewById(R.id.price)
        private val thumbnail:ImageView = itemView.findViewById(R.id.thumbnail)
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"

        private var currentProduct:Product?=null

        init {
            itemView.setOnClickListener {
                currentProduct?.let { product ->
                    onClick(product)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_product,parent,false)
        return NewProductViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: NewProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}
object NewProductCallBack:DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

}