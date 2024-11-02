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
import com.example.ecommerce.api.model.Cart

class PesananAdapter(private val onClick:(Cart) -> Unit):ListAdapter<Cart,PesananAdapter.PesananViewHolder>(PesananCallBack){
    class PesananViewHolder(itemView : View,val onClick: (Cart) -> Unit):RecyclerView.ViewHolder(itemView){
        private val idPrimary : TextView = itemView.findViewById(R.id.id_primary)
        private val idProduct : TextView = itemView.findViewById(R.id.text_id)
        private val productName : TextView = itemView.findViewById(R.id.product_name)
        private val productPrice : TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity : TextView = itemView.findViewById(R.id.product_quantity)
        private val category : TextView = itemView.findViewById(R.id.category)
        private val btnKrm : Button = itemView.findViewById(R.id.btn_krm)

        private var currentCart : Cart?=null
        init {
            btnKrm.setOnClickListener{
                currentCart?.let{
                    cart -> onClick(cart)
                }
            }
        }
        fun bind(cart: Cart){
            currentCart = cart
            idPrimary.text = cart.id
            idProduct.text = cart.product.id
            productName.text = cart.product.productName
            productPrice.text = cart.product.productPrice.toString()
            productQuantity.text = cart.product.productQuantity.toString()
            category.text = cart.product.category
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan,parent,false)
        return PesananViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val cart = getItem(position)
        holder.bind(cart)
    }

}
object PesananCallBack : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.id == newItem.id
    }

}