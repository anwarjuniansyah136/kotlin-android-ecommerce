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

class EditSupplyProductActivity : AppCompatActivity() {
    private lateinit var editProductName:EditText
    private lateinit var editProductPrice:EditText
    private lateinit var editProductQuantity:EditText
    private lateinit var editDate:EditText
    private lateinit var editComopanyName:EditText
    private lateinit var editCategory:EditText
    private lateinit var btnSubmit:Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call : Call<LoginResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_supply_product)
//        editProductName = findViewById(R.id.product_name)
//        editProductPrice = findViewById(R.id.product_price)
//        editProductQuantity = findViewById(R.id.product_quantity)
//        editDate = findViewById(R.id.date)
//        editComopanyName = findViewById(R.id.company_name)
//        editCategory = findViewById(R.id.category)
//        btnSubmit = findViewById(R.id.btn_edit)

//        btnSubmit.setOnClickListener{
//            ApiClient.init(this)
//            sessionManager = SessionManager(this)
//            val id = intent.getStringExtra("supply_id")
//            val token = sessionManager.getAuthToken()
//            val supply = SupplyProductRequest(
//                editProductName.text.toString(),
//                editProductPrice.text.toString().toInt(),
//                editProductQuantity.text.toString().toInt(),
//                editDate.text.toString(),
//                editComopanyName.text.toString(),
//                editCategory.text.toString()
//                )
//            call = ApiClient.supplyService.updateSupply("Bearer $token",id.toString(),supply)
//            call.enqueue(object : Callback<LoginResponse>{
//                override fun onResponse(
//                    call: Call<LoginResponse>,
//                    response: Response<LoginResponse>
//                ) {
//                    if(response.isSuccessful){
//                        Toast.makeText(this@EditSupplyProductActivity,"Success To Update Supply",Toast.LENGTH_SHORT).show()
//                    }
//                    else{
//                        Toast.makeText(this@EditSupplyProductActivity,"Failed Update Supply",Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }
    }
}