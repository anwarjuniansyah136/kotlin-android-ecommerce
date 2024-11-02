package com.example.ecommerce

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
import com.example.ecommerce.api.adapter.CartAdapter
import com.example.ecommerce.api.model.Cart
import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.model.Product
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        swipeRefresh = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)

        cartAdapter = CartAdapter{
                cart -> cartOnclick(cart)
        }
        recyclerView.adapter = cartAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefresh.setOnRefreshListener { getData() }
        getData()
    }

    private fun cartOnclick(cart: Cart){
        Toast.makeText(applicationContext,cart.product.productName,Toast.LENGTH_SHORT).show()
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
}