package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.ProductAdapter
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductResponse
import com.example.ecommerce.api.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<ProductResponse>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var cartIcon: ImageView
    private lateinit var searchInput: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView

    private var fullProductList: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(this)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recycler_view)
        cartIcon = findViewById(R.id.cart_icon)
        searchInput = findViewById(R.id.search_input)
        progressBar = findViewById(R.id.progress_bar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        productAdapter = ProductAdapter { product -> productOnClick(product) }
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
        }

        cartIcon.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home->{
                    true
                }
                R.id.nav_new_products->{
                    startActivity(Intent(this,NewProductActivity::class.java))
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

    private fun productOnClick(product: Product) {
        Toast.makeText(this, "Clicked: ${product.productName}", Toast.LENGTH_SHORT).show()
    }

    private fun getData() {
        progressBar.visibility = View.VISIBLE
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()

        progressBar.visibility = ProgressBar.VISIBLE // Show progress bar
        call = ApiClient.productService.getAll("Bearer $token")
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                progressBar.visibility = ProgressBar.GONE // Hide progress bar
                if (response.isSuccessful) {
                    val products = response.body()?.data ?: emptyList()
                    fullProductList = products
                    productAdapter.submitList(products)
                } else {
                    showError("Failed to load products: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE // Hide progress bar
                showError("Error: ${t.localizedMessage}")
            }
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullProductList  // If query is empty, show all products
        } else {
            fullProductList.filter { product ->
                product.productName.contains(query, ignoreCase = true)
            }
        }
        productAdapter.submitList(filteredList)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::call.isInitialized && !call.isCanceled) {
            call.cancel()  // Cancel API call if still running
        }
    }
}
