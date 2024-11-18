package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.CartAdapter
import com.example.ecommerce.api.model.Cart
import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var call: Call<CartResponse>
    private lateinit var sessionManager:SessionManager
    private lateinit var calls: Call<PostResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        swipeRefresh = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)
        cartAdapter = CartAdapter(
            onBuy = { cart ->
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                if(token != null){
                    showQuantityDialog(cart,cart.product.productPrice, token)
                }
            },
            onCancel = { cart ->
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                calls = ApiClient.cartService.deleteCart("Bearer $token",cart.id)
                calls.enqueue(object : Callback<PostResponse>{
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@CartActivity,"Pesanan Dibatalkan",Toast.LENGTH_SHORT).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@CartActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }, 1000)
                        }
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        )
        recyclerView.adapter = cartAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefresh.setOnRefreshListener { getData() }
        getData()
    }
    private fun getData(){
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken();
        swipeRefresh.isRefreshing = true
        call = ApiClient.cartService.getAll("Bearer $token")
        call.enqueue(object : Callback<CartResponse>{
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if(response.isSuccessful){
                    swipeRefresh.isRefreshing = false
                    val cart = response.body()?.data
                    cartAdapter.submitList(cart)
//                    Toast.makeText(this@CartActivity,"Fetch data success",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@CartActivity,"Failed Fetch Data",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(applicationContext,t.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun showQuantityDialog(cart: Cart, pricePerItem: Int, token: String?) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_quantity, null)
        builder.setView(dialogView)

        val quantityInput = dialogView.findViewById<EditText>(R.id.quantity_input)
        val totalPriceText = dialogView.findViewById<TextView>(R.id.total_price)

        quantityInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val quantity = s.toString().toIntOrNull() ?: 0
                val totalPrice = quantity * pricePerItem
                totalPriceText.text = "Total: Rp $totalPrice"
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        builder.setPositiveButton("OK") { dialog, _ ->
            val quantityText = quantityInput.text.toString()
            val quantity = quantityText.toIntOrNull() ?: 1
            dialog.dismiss()
            if (token != null) {
                performCheckout(cart, quantity, token)
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
    private fun performCheckout(cart: Cart, quantity: Int, token: String) {
        if(quantity > cart.product.productQuantity){
            Toast.makeText(this,"Stock tidak mencukupi",Toast.LENGTH_SHORT).show()
        }else{
            calls = ApiClient.productService.minStock("Bearer $token",cart.product.id,quantity)
            calls.enqueue(object : Callback<PostResponse>{
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if(response.isSuccessful){
                        calls = ApiClient.checkOutService.postCheckOut("Bearer $token", cart.product.id, quantity)
                        calls.enqueue(object : Callback<PostResponse> {
                            override fun onResponse(
                                call: Call<PostResponse>,
                                response: Response<PostResponse>
                            ) {
                                if (response.isSuccessful) {
                                    calls = ApiClient.cartService.deleteCart("Bearer $token", cart.id)
                                    calls.enqueue(object : Callback<PostResponse> {
                                        override fun onResponse(
                                            call: Call<PostResponse>,
                                            response: Response<PostResponse>
                                        ) {

                                        }

                                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                    Toast.makeText(
                                        this@CartActivity,
                                        "Pesanan Anda Sedang dalam Pengiriman",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val intent = Intent(this@CartActivity, HomeActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }, 1000)
                                }
                            }
                            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}