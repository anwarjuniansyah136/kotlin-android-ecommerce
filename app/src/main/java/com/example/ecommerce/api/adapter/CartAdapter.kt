package com.example.ecommerce.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.api.model.Cart
import com.example.ecommerce.R


class CartAdapter(private val onBuy:(Cart)->Unit,private val onCancel:(Cart)->Unit):ListAdapter<Cart,CartAdapter.CartViewHolder>(CartCallBack){
    class CartViewHolder(itemView:View,val onBuy: (Cart) -> Unit,onCancel: (Cart) -> Unit):
    RecyclerView.ViewHolder(itemView){
        private val thumbnail : ImageView = itemView.findViewById(R.id.thumbnail)
        private val title : TextView = itemView.findViewById(R.id.title)
        private val price : TextView = itemView.findViewById(R.id.price)
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"
        private val btnBuy : Button = itemView.findViewById(R.id.checkout)
        private val btnCancel : Button = itemView.findViewById(R.id.btn_cancel)

        private var currentCart: Cart? = null

        init {
            btnBuy.setOnClickListener{
                currentCart?.let {
                    cart -> onBuy(cart)
                }
            }
            btnCancel.setOnClickListener{
                currentCart?.let {
                    cart -> onCancel(cart)
                }
            }
        }

        fun bind(cart:Cart){
            currentCart = cart
            title.text = cart.product.productName
            price.text = cart.product.productPrice.toString()
            val imageUrl = BASE_URL + cart.product.image
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return CartViewHolder(view,onBuy,onCancel)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val cart = getItem(position)
        holder.bind(cart)
    }

}
object CartCallBack:DiffUtil.ItemCallback<Cart>(){
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.id == newItem.id
    }
}