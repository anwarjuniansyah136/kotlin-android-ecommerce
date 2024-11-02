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
import com.example.ecommerce.api.model.Kurir

class KurirAdapter(private val onClick: (Kurir) -> Unit) : ListAdapter<Kurir, KurirAdapter.KurirViewHolder>(KurirCallBack) {

    class KurirViewHolder(itemView: View, private val onClick: (Kurir) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.text_name)
        private val btnKirim : Button = itemView.findViewById(R.id.btn_krm)
        private var currentKurir: Kurir? = null

        init {
            btnKirim.setOnClickListener{
                currentKurir?.let {
                    kurir -> onClick(kurir)
                }
            }
        }

        fun bind(kurir: Kurir) {
            currentKurir = kurir
            textName.text = kurir.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KurirViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose, parent, false)
        return KurirViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: KurirViewHolder, position: Int) {
        val kurir = getItem(position)
        holder.bind(kurir)
    }
}

object KurirCallBack : DiffUtil.ItemCallback<Kurir>() {
    override fun areItemsTheSame(oldItem: Kurir, newItem: Kurir): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Kurir, newItem: Kurir): Boolean {
        return oldItem == newItem
    }
}
