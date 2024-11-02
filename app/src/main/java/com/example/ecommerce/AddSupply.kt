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
import com.example.ecommerce.api.model.SupplyProductRequest
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSupply : AppCompatActivity() {
    private lateinit var editProductName:EditText
    private lateinit var editProductPrice:EditText
    private lateinit var editProductQuantity:EditText
    private lateinit var editDate:EditText
    private lateinit var editCompanyName:EditText
    private lateinit var editCategory:EditText
    private lateinit var btnSubmit:Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<LoginResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_supply)
        editProductName = findViewById(R.id.product_name)
        editProductPrice = findViewById(R.id.product_price)
        editProductQuantity = findViewById(R.id.product_quantity)
        editDate = findViewById(R.id.date)
        editCompanyName = findViewById(R.id.company_name)
        editCategory = findViewById(R.id.category)
        btnSubmit = findViewById(R.id.btn_add)
        btnSubmit.setOnClickListener{
            ApiClient.init(this)
            sessionManager = SessionManager(this)
            val token = sessionManager.getAuthToken()
            val supply = SupplyProductRequest(
                editProductName.text.toString(),
                editProductPrice.text.toString().toInt(),
                editProductQuantity.text.toString().toInt(),
                editDate.text.toString(),
                editCompanyName.text.toString(),
                editCategory.text.toString())
            call = ApiClient.supplyService.postSupply("Bearer $token",supply)
            call.enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(this@AddSupply,"Success to Add Supply",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}