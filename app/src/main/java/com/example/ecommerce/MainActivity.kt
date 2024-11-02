package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    lateinit var btnLogin:Button
    lateinit var btnRegister:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}