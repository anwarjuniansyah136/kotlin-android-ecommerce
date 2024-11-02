package com.example.ecommerce.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.api.model.SupplyProduct
import com.example.ecommerce.R

class SupplyAdapter(private val onEditClick:(SupplyProduct) -> Unit,private val onDeleteClick:(SupplyProduct) -> Unit):ListAdapter<SupplyProduct,SupplyAdapter.SupplyViewHolder>(SupplyCallBack) {
    class SupplyViewHolder(itemView:View,onEdit: (SupplyProduct) -> Unit,onDelete: (SupplyProduct) -> Unit): RecyclerView.ViewHolder(itemView){
        private val id:TextView = itemView.findViewById(R.id.text_id)
        private val productName:TextView = itemView.findViewById(R.id.product_name)
        private val productPrice:TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity:TextView = itemView.findViewById(R.id.product_quantity)
        private val category:TextView = itemView.findViewById(R.id.category)
        private val companyName:TextView = itemView.findViewById(R.id.company_name)
        private val date:TextView = itemView.findViewById(R.id.date)
        private val btnEdit:Button = itemView.findViewById(R.id.btn_edit)
        private val btnDelete:Button = itemView.findViewById(R.id.btn_dlt)

        private var currentSupply:SupplyProduct? = null
        init {
//            itemView.setOnClickListener{
                btnEdit.setOnClickListener{
                    currentSupply?.let {
                        supplyProduct -> onEdit(supplyProduct)
                    }
                }
                btnDelete.setOnClickListener{
                    currentSupply?.let {
                        supplyProduct -> onDelete(supplyProduct)
                    }
                }
//            }
        }
        fun bind(supply:SupplyProduct){
            currentSupply = supply
            id.text = supply.id
            productName.text = supply.productName
            productPrice.text = supply.productPrice.toString()
            productQuantity.text = supply.productQuantity.toString()
            category.text = supply.category
            companyName.text = supply.companyName
            date.text = supply.date
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_supply,parent,false)
        return SupplyViewHolder(view,onEditClick,onDeleteClick)
    }

    override fun onBindViewHolder(holder: SupplyViewHolder, position: Int) {
        val supply = getItem(position)
        holder.bind(supply)
    }
}
object SupplyCallBack:DiffUtil.ItemCallback<SupplyProduct>(){
    override fun areItemsTheSame(oldItem: SupplyProduct, newItem: SupplyProduct): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SupplyProduct, newItem: SupplyProduct): Boolean {
        return oldItem.id == newItem.id
    }

}