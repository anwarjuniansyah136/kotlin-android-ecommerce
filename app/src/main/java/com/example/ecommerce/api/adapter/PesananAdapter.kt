package com.example.ecommerce.api.adapter

import android.annotation.SuppressLint
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
import com.example.ecommerce.R
import com.example.ecommerce.api.model.CheckOut

class PesananAdapter(private val onClick:(CheckOut) -> Unit):ListAdapter<CheckOut,PesananAdapter.PesananViewHolder>(PesananCallBack){
    class PesananViewHolder(itemView : View,val onClick: (CheckOut) -> Unit):RecyclerView.ViewHolder(itemView){
        private val productName : TextView = itemView.findViewById(R.id.product_name)
        private val productPrice : TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity : TextView = itemView.findViewById(R.id.product_quantity)
        private val btnKrm : Button = itemView.findViewById(R.id.btn_krm)
        private val thumbnail : ImageView = itemView.findViewById(R.id.thumbnail)
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"

        private var currentCheckOut : CheckOut?=null
        init {
            btnKrm.setOnClickListener{
                currentCheckOut?.let{
                    checkout -> onClick(checkout)
                }
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(checkOut: CheckOut){
            currentCheckOut = checkOut
            productName.text = checkOut.productName
            productPrice.text = "Rp ${(checkOut.productPrice*checkOut.sum)}"
            productQuantity.text = "${ checkOut.sum } unit"
            val imageUrl = BASE_URL + checkOut.image
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan,parent,false)
        return PesananViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val checkout = getItem(position)
        holder.bind(checkout)
    }

}
object PesananCallBack : DiffUtil.ItemCallback<CheckOut>() {
    override fun areItemsTheSame(oldItem: CheckOut, newItem: CheckOut): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CheckOut, newItem: CheckOut): Boolean {
        return oldItem.productId == newItem.productId
    }

}