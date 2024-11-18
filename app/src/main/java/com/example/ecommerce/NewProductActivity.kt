package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.NewProductAdapter
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductResponse
import com.example.ecommerce.api.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewProductActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var newProductAdapter: NewProductAdapter
    private lateinit var call:Call<ProductResponse>
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)
        swipeRefreshLayout.isRefreshing = true

        newProductAdapter = NewProductAdapter(
            onClick = {product ->  onClickProduct(product)

            }
        )
        recyclerView.adapter = newProductAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefreshLayout.setOnRefreshListener { getData() }
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home->{
                    startActivity(Intent(this,HomeActivity::class.java))
                    true
                }
                R.id.nav_new_products->{
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this,ProfilAcivity::class.java))
                    true
                }
                else -> false
            }
        }
        getData()
    }

    private fun onClickProduct(product: Product){
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("product_id", product.id)
        intent.putExtra("product_name", product.productName)
        intent.putExtra("product_quantity", product.productQuantity)
        intent.putExtra("product_price", product.productPrice)
        intent.putExtra("product_category", product.category)
        intent.putExtra("product_image", product.image)
        startActivity(intent)
    }

    private fun getData(){
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        call = ApiClient.productService.getNewProduct("Bearer $token")
        call.enqueue(object : Callback<ProductResponse>{
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful){
                    swipeRefreshLayout.isRefreshing = false
                    newProductAdapter.submitList(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}