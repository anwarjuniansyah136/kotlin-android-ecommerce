package com.example.ecommerce

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.ProductRequest
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProduct : AppCompatActivity() {
    private lateinit var productName:EditText
    private lateinit var productPrice:EditText
    private lateinit var productQuantity:EditText
    private lateinit var category:EditText
    private lateinit var btnAdd:Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<LoginResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        productName = findViewById(R.id.product_name)
        productPrice = findViewById(R.id.product_price)
        productQuantity = findViewById(R.id.product_quantity)
        category = findViewById(R.id.category)
        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener{
            val product = ProductRequest(productName.text.toString(),productPrice.text.toString().toInt(),productQuantity.text.toString().toInt(),category.text.toString())
            ApiClient.init(this)
            sessionManager = SessionManager(this)
            val token = sessionManager.getAuthToken()
            call = ApiClient.productService.postProduct("Bearer $token",product)
            call.enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(this@AddProduct,"Success to add Product",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@AddProduct,"Failed Add Product",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}