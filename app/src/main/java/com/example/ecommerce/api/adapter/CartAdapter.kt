package com.example.ecommerce.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.api.model.Cart
import com.example.ecommerce.R


class CartAdapter(private val onClick:(Cart)->Unit):ListAdapter<Cart,CartAdapter.CartViewHolder>(CartCallBack){
    class CartViewHolder(itemView:View,val onClick: (Cart) -> Unit):
    RecyclerView.ViewHolder(itemView){
        private val thumbnail : ImageView = itemView.findViewById(R.id.thumbnail)
        private val title : TextView = itemView.findViewById(R.id.title)
        private val brand : TextView = itemView.findViewById(R.id.brand)
        private val price : TextView = itemView.findViewById(R.id.price)

        private var currentCart: Cart? = null

        init {
            itemView.setOnClickListener{
                currentCart?.let{
                    onClick(it)
                }
            }
        }

        fun bind(cart:Cart){
            currentCart = cart

            title.text = cart.product.productName
            brand.text = cart.product.category
            price.text = cart.product.productPrice.toString()

            Glide.with(itemView).load(cart.product.image).centerCrop().into(thumbnail)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return CartViewHolder(view,onClick)
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