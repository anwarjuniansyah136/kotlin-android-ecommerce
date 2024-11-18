package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.ProfilResponse
import com.example.ecommerce.api.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilAcivity : AppCompatActivity() {
    private lateinit var thumbnail:ImageView
    private lateinit var username:TextView
    private lateinit var email:TextView
    private lateinit var call:Call<ProfilResponse>
    private val BASE_URL = "http://10.0.2.2:8080/auth/photos/"
    private lateinit var sessionManager: SessionManager
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_acivity)
        thumbnail = findViewById(R.id.profile_image)
        username = findViewById(R.id.username)
        email = findViewById(R.id.user_email)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home->{
                    startActivity(Intent(this,HomeActivity::class.java))
                    true
                }
                R.id.nav_new_products->{
                    startActivity(Intent(this,NewProductActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }
        getUserProfil()
    }

    private fun getUserProfil(){
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        call = ApiClient.loginService.profil("Bearer $token")
        call.enqueue(object : Callback<ProfilResponse>{
            override fun onResponse(
                call: Call<ProfilResponse>,
                response: Response<ProfilResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.let { data ->
                        username.text = data.name
                        email.text = data.email
                        val imageUrl = BASE_URL + data.image
                        Glide.with(this@ProfilAcivity)
                            .load(imageUrl)
                            .centerCrop()
                            .into(thumbnail)
                    }
                } else {
                    Log.d("ProfilActivity", "Response Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ProfilAcivity, "Failed to load profile data", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}