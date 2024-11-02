package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.LoginRequest
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var editEmail:EditText
    private lateinit var editPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnRegis:Button
    private lateinit var call:Call<LoginResponse>
    private lateinit var sessionManager:SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        btnRegis = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        btnRegis.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener{
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                val lgn = LoginRequest(email,password)
                login(lgn)
            }
            else{
                Toast.makeText(this,"Tolong isi semua kolom", LENGTH_SHORT).show()
            }
        }
    }
    private fun login(request: LoginRequest){
        call = ApiClient.loginService.login(request)
        call.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body() != null && response.isSuccessful){
                    val loginResponse = response.body()!!
                    sessionManager = SessionManager(this@LoginActivity)
                    sessionManager.saveAuthToken(loginResponse.data.token)
                    if(loginResponse.data.role == "users"){
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                    }else if(loginResponse.data.role == "gudang"){
                        startActivity(Intent(this@LoginActivity,GudangActivity::class.java))
                    }else if(loginResponse.data.role == "sender"){
                        startActivity(Intent(this@LoginActivity,KurirActivity::class.java))
                    }
                }else{
                    Toast.makeText(this@LoginActivity,"Username atau Password anda salah",
                        LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}