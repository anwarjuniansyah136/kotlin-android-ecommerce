package com.example.ecommerce

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.ProductRequest
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProductActivity : AppCompatActivity() {
    private lateinit var editProductName : EditText
    private lateinit var editProductPrice : EditText
    private lateinit var editProductQuantity : EditText
    private lateinit var editCategory : EditText
    private lateinit var btnSubmit : Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call : Call<LoginResponse>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        editProductName = findViewById(R.id.product_name)
        editProductPrice = findViewById(R.id.product_price)
        editProductQuantity = findViewById(R.id.product_quantity)
        editCategory = findViewById(R.id.category)
        btnSubmit = findViewById(R.id.btn_edit)
        val id = intent.getStringExtra("product_id")
        val prName = intent.getStringExtra("product_name")
        val prPrice = intent.getStringExtra("product_price")
        val prQuantity = intent.getStringExtra("product_quantity")
        val cg = intent.getStringExtra("category")
        editProductName.setText(prName.toString())
        editProductPrice.setText(prPrice.toString())
        editProductQuantity.setText(prQuantity.toString())
        editCategory.setText(cg.toString())
        btnSubmit.setOnClickListener{
            val productName = editProductName.text
            val productPrice = editProductPrice.text
            val productQuantity = editProductQuantity.text
            val category = editCategory.text

            ApiClient.init(this)
            sessionManager = SessionManager(this)
            val token = sessionManager.getAuthToken()
            val productRequest = ProductRequest(productName.toString(),productPrice.toString().toInt(),productQuantity.toString().toInt(),category.toString())
            call = ApiClient.productService.updateProduct("Bearer $token",productRequest,id.toString())
            call.enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(this@EditProductActivity,"Success Update Product",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@EditProductActivity,"Failed Update Product",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}