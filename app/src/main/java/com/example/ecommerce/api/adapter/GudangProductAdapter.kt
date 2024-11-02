package com.example.ecommerce.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product

class GudangProductAdapter(
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : ListAdapter<Product, GudangProductAdapter.GudangProductViewHolder>(GudangProductCallBack) {

    class GudangProductViewHolder(itemView: View,
                                  private val onEditClick: (Product) -> Unit,
                                  private val onDeleteClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val id: TextView = itemView.findViewById(R.id.text_id)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val btnEdit: View = itemView.findViewById(R.id.btn_edit)
        private val btnDelete: View = itemView.findViewById(R.id.btn_dlt)

        private var currentProduct: Product? = null

        init {
            btnEdit.setOnClickListener {
                currentProduct?.let { product ->
                    onEditClick(product)
                }
            }
            btnDelete.setOnClickListener {
                currentProduct?.let { product ->
                    onDeleteClick(product)
                }
            }
        }

        fun bind(product: Product) {
            currentProduct = product
            id.text = product.id
            productName.text = product.productName
            productQuantity.text = product.productQuantity.toString()
            productPrice.text = product.productPrice.toString()
            category.text = product.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GudangProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_gudang, parent, false)
        return GudangProductViewHolder(view, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: GudangProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}


object GudangProductCallBack:DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

}