package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GudangActivity : AppCompatActivity() {
    private lateinit var btnPenerima:Button
    private lateinit var btnProduct:Button
    private lateinit var btnPesanan:Button
    private lateinit var btnAdd:Button
    private lateinit var btnCategory:Button
    private lateinit var addCategory:Button
    private lateinit var btnAddSupply:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gudang)
        btnPenerima = findViewById(R.id.btn_pnrm)
        btnProduct = findViewById(R.id.btn_product)
        btnAdd = findViewById(R.id.btn_add)
        btnCategory = findViewById(R.id.btn_category)
        addCategory = findViewById(R.id.btn_add_category)
        btnAddSupply = findViewById(R.id.btn_add_supply)
        btnPesanan = findViewById(R.id.btn_pesan)
        btnPenerima.setOnClickListener{
            startActivity(Intent(this,SupplyProductActivity::class.java))
        }
        btnProduct.setOnClickListener{
            startActivity(Intent(this,ProductGudangActivity::class.java))
        }
        btnPesanan.setOnClickListener{
            startActivity(Intent(this,PesananActivity::class.java))
        }
        btnAdd.setOnClickListener{
            startActivity(Intent(this,AddProduct::class.java))
        }
        btnCategory.setOnClickListener{
            startActivity(Intent(this,CategoryActivity::class.java))
        }
        addCategory.setOnClickListener{
            startActivity(Intent(this,AddCategory::class.java))
        }
        btnAddSupply.setOnClickListener{
            startActivity(Intent(this,AddSupply::class.java))
        }
    }
}