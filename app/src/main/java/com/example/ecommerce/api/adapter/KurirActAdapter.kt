package com.example.ecommerce.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product

class KurirActAdapter(private val onClik:(Product) -> Unit):ListAdapter<Product,KurirActAdapter.KurirActViewHolder>(KurirActCallBack){
    class  KurirActViewHolder(itemView:View,private val onClik: (Product) -> Unit):RecyclerView.ViewHolder(itemView){
        private val productName:TextView = itemView.findViewById(R.id.product_name)
        private val productPrice:TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity:TextView = itemView.findViewById(R.id.product_quantity)
        private val category:TextView = itemView.findViewById(R.id.category)
        private val btnSuccess:Button = itemView.findViewById(R.id.btn_success)

        private var currentProduct:Product?=null
        init {
            btnSuccess.setOnClickListener{
                currentProduct?.let { product -> onClik(product) }
            }
        }
        fun bind(product: Product){
            currentProduct = product
            productName.text = product.productName
            productPrice.text = product.productPrice.toString()
            productQuantity.text = product.productQuantity.toString()
            category.text = product.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KurirActViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir,parent,false)
        return KurirActViewHolder(view,onClik)
    }

    override fun onBindViewHolder(holder: KurirActViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
object KurirActCallBack:DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

}