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
import com.example.ecommerce.api.model.CategoryRequest
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCategory : AppCompatActivity() {
    private lateinit var editCategory:EditText
    private lateinit var btnSubmit:Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<LoginResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        editCategory = findViewById(R.id.category)
        btnSubmit = findViewById(R.id.btn_add)
        btnSubmit.setOnClickListener{
            ApiClient.init(this)
            sessionManager = SessionManager(this)
            val token = sessionManager.getAuthToken()
            val category = CategoryRequest(editCategory.text.toString())
            call = ApiClient.categoryService.postCategory("Bearer $token",category)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(this@AddCategory,"Success to Add Category",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}