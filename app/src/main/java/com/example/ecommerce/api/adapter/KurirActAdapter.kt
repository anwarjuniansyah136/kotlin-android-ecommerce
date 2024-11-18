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
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Delivery
import com.example.ecommerce.api.model.Product

class KurirActAdapter(private val onClikSuccess:(Delivery) -> Unit,private val onClickBack:(Delivery) -> Unit):ListAdapter<Delivery,KurirActAdapter.KurirActViewHolder>(KurirActCallBack){
    class  KurirActViewHolder(itemView:View,private val onClikSuccess: (Delivery) -> Unit,private val onClickBack:(Delivery) -> Unit):RecyclerView.ViewHolder(itemView){
        private val productName:TextView = itemView.findViewById(R.id.product_name)
        private val productPrice:TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity:TextView = itemView.findViewById(R.id.product_quantity)
        private val kurirName:TextView = itemView.findViewById(R.id.kurir_name)
        private val btnSuccess:Button = itemView.findViewById(R.id.btn_success)
        private val btnBack:Button = itemView.findViewById(R.id.btn_failed)
        private val thumbnail:ImageView = itemView.findViewById(R.id.thumbnail)
        private val BASE_URL = "http://10.0.2.2:8080/product/photos/"

        private var currentProduct:Delivery?=null
        init {
            btnSuccess.setOnClickListener{
                currentProduct?.let { delivery -> onClikSuccess(delivery) }
            }
            btnBack.setOnClickListener{
                currentProduct?.let { delivery -> onClickBack(delivery) }
            }
        }
        fun bind(delivery: Delivery){
            currentProduct = delivery
            productName.text = delivery.productName
            productPrice.text = delivery.productPrice.toString()
            productQuantity.text = delivery.sum.toString()
            kurirName.text = delivery.kurirName
            val imageUrl = BASE_URL +  delivery.image
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KurirActViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir,parent,false)
        return KurirActViewHolder(view,onClikSuccess,onClickBack)
    }

    override fun onBindViewHolder(holder: KurirActViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
object KurirActCallBack:DiffUtil.ItemCallback<Delivery>(){
    override fun areItemsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
        return oldItem.productId == newItem.productId
    }

}