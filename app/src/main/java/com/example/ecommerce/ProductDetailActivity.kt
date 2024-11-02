package com.example.ecommerce

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var btnAddCart : Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<CartResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Ambil data produk satu per satu
        val productId: String? = intent.getStringExtra("product_id")
        val productName: String? = intent.getStringExtra("product_name")
        val productQuantity: Int = intent.getIntExtra("product_quantity", 0)
        val productPrice: Int = intent.getIntExtra("product_price", 0)
        val productCategory: String? = intent.getStringExtra("product_category")
        val productImage: String? = intent.getStringExtra("product_image")

        // Tampilkan data produk di layout
        findViewById<TextView>(R.id.detail_title).text = productName
        findViewById<TextView>(R.id.detail_quantity).text = productQuantity.toString()
        findViewById<TextView>(R.id.detail_price).text = productPrice.toString()
        findViewById<TextView>(R.id.detail_category).text = productCategory
        Glide.with(this).load(productImage).into(findViewById(R.id.detail_thumbnail))
        btnAddCart = findViewById(R.id.btn_add_to_cart)
        sessionManager = SessionManager(this)
        ApiClient.init(this)
        val token = sessionManager.getAuthToken()
        btnAddCart.setOnClickListener{
            if (token != null && productId != null) {
               call = ApiClient.cartService.postCart("Bearer $token",productId)
                call.enqueue(object : Callback<CartResponse>{
                    override fun onResponse(
                        call: Call<CartResponse>,
                        response: Response<CartResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@ProductDetailActivity,"Success To Add Cart",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ProductDetailActivity,"Failed To Add Cart",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                Toast.makeText(this,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
