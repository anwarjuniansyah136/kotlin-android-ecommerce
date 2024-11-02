package com.example.ecommerce

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ZeroActivity : AppCompatActivity() {
    lateinit var tokenTextView:TextView
    lateinit var emailTextView: TextView
    lateinit var roleTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zero)
        val token = intent.getStringExtra("TOKEN")
        val email = intent.getStringExtra("EMAIL")
        val role = intent.getStringExtra("ROLE")

        tokenTextView = findViewById(R.id.tokenTextView)
        emailTextView = findViewById(R.id.emailTextView)
        roleTextView = findViewById(R.id.roleTextView)

        tokenTextView.text = "Token : ${token}"
        emailTextView.text = "Email : ${email}"
        roleTextView.text = "Role : ${role}"
    }
}