package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.GudangProductAdapter
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductGudangActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call:Call<ProductResponse>
    private lateinit var gudangProductAdapter: GudangProductAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var calls:Call<LoginResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_gudang)
        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)

        gudangProductAdapter = GudangProductAdapter(
            onEditClick = { product ->
                val intent = Intent(this, EditProductActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onDeleteClick = { product ->
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                calls = ApiClient.productService.deleteProduct("Bearer $token",product.id)
                calls.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@ProductGudangActivity,"Success Delete Product",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ProductGudangActivity,"Failed Delete Product",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        )


        recyclerView.adapter = gudangProductAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)

        swipeRefreshLayout.setOnRefreshListener { getData() }
        getData()
    }

    private fun gudangProductOnClick(product: Product){
        Toast.makeText(applicationContext,product.productName,Toast.LENGTH_SHORT).show()
    }

    private fun getData(){
        swipeRefreshLayout.isRefreshing = true
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        call = ApiClient.productService.getAll("Bearer $token");
        call.enqueue(object : Callback<ProductResponse>{
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful){
                    swipeRefreshLayout.isRefreshing = false
                    val product = response.body()!!.data
                    gudangProductAdapter.submitList(product)
                }else{
                    Toast.makeText(this@ProductGudangActivity,"Failed Fetch Data",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}