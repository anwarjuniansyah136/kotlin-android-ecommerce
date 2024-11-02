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
import com.example.ecommerce.api.model.Category

class CategoryAdapter(
    private val onEdit:(Category) -> Unit,
    private val onDelete:(Category) -> Unit
    ):ListAdapter<Category,CategoryAdapter.CategoryViewHolder>(CategoryCallBack){
        class CategoryViewHolder(itemView:View, private val onEdit: (Category) -> Unit, private val onDelete: (Category) -> Unit):RecyclerView.ViewHolder(itemView){
            private val id : TextView = itemView.findViewById(R.id.text_id)
            private val textCategory : TextView = itemView.findViewById(R.id.category)
            private val btnEdit : Button = itemView.findViewById(R.id.btn_edit)
            private val btnDelete : Button = itemView.findViewById(R.id.btn_dlt)

            private var currentCategory:Category?=null
            init {
                btnEdit.setOnClickListener{
                    currentCategory?.let {
                        category -> onEdit(category)
                    }
                }
                btnDelete.setOnClickListener{
                    currentCategory?.let {
                        category -> onDelete(category)
                    }
                }
            }

            fun bind(category:Category){
                currentCategory = category
                id.text = category.id
                textCategory.text = category.categoryName
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(view,onEdit,onDelete)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

}
object CategoryCallBack:DiffUtil.ItemCallback<Category>(){
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

}