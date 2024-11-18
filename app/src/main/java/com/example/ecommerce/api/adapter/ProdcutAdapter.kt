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
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.R

class ProductAdapter(private val onClick:(Product)->Unit) :
    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductCallBack){
    class ProductViewHolder(itemView:View ,val onClick: (Product) -> Unit):
        RecyclerView.ViewHolder(itemView){
        private val thumbnail : ImageView = itemView.findViewById(R.id.thumbnail)
        private val title : TextView = itemView.findViewById(R.id.title)
        private val brand : TextView = itemView.findViewById(R.id.brand)
        private val price : TextView = itemView.findViewById(R.id.price)
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"
        private var currentProduct : Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let { product ->
                    val intent = Intent(itemView.context, ProductDetailActivity::class.java)
                    intent.putExtra("product_id", product.id)
                    intent.putExtra("product_name", product.productName)
                    intent.putExtra("product_quantity", product.productQuantity)
                    intent.putExtra("product_price", product.productPrice)
                    intent.putExtra("product_category", product.category)
                    intent.putExtra("product_image", product.image)
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(product: Product){
            currentProduct = product

            title.text = product.productName
            brand.text = product.category
            price.text = "Rp ${product.productPrice}"
            val imageUrl = BASE_URL + product.image
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return ProductViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

    }
}
object ProductCallBack : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

}