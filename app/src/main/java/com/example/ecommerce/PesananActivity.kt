package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.PesananAdapter
import com.example.ecommerce.api.model.Cart
import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var pesananAdapter: PesananAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)

        pesananAdapter = PesananAdapter(onClick = { cart ->
            val intent = Intent(this, ChooseKurirActivity::class.java)
            intent.putExtra("product_id", cart.product.id)
            startActivity(intent)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pesananAdapter

        swipeRefreshLayout.setOnRefreshListener { getData() }

        getData()
    }

    private fun getData() {
        swipeRefreshLayout.isRefreshing = true
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()

        ApiClient.init(this)
        val call = ApiClient.cartService.getAll("Bearer $token")

        call.enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val cartList = response.body()?.data ?: emptyList()
                    Toast.makeText(this@PesananActivity, "Data loaded: ${cartList.size} items", Toast.LENGTH_SHORT).show()
                    pesananAdapter.submitList(cartList)
                } else {
                    Toast.makeText(this@PesananActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@PesananActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
