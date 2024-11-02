package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var editFullName:EditText
    lateinit var editEmail:EditText
    lateinit var editPassword:EditText
    lateinit var editPassConf:EditText
    lateinit var btnRegis:Button
    lateinit var btnLogin:Button
    lateinit var call: Call<Void>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editFullName = findViewById(R.id.full_name)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        editPassConf = findViewById(R.id.password_conf)
        btnRegis = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

        btnRegis.setOnClickListener{
            val fullName = editFullName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if(editFullName.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty() && editPassConf.text.isNotEmpty()){
                if(editPassword.text.toString() == editPassConf.text.toString()){
                    val register = RegisterRequest(fullName,email,password)
                    registerUser(register)
                }
                else{
                    Toast.makeText(this,"Konfirmasi kata sandi salah",LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Tolong isi semua kolom", LENGTH_SHORT).show()
            }
        }
        btnLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun registerUser(request: RegisterRequest) {
        call = ApiClient.registrasiService.create(request)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call:Call<Void>,response:Response<Void>){
                if(response.isSuccessful){
                    Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RegisterActivity, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
//        ApiClient.instance.create(request).enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@RegisterActivity, "Registrasi gagal", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}