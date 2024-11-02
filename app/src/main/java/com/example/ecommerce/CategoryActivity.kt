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
import com.example.ecommerce.api.adapter.CategoryAdapter
import com.example.ecommerce.api.model.CategoryResponse
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var call : Call<CategoryResponse>
    private lateinit var calls : Call<LoginResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)
        
        categoryAdapter = CategoryAdapter(
            onEdit = { category ->
                val intent = Intent(this,UpdateCategoryActivity::class.java)
                intent.putExtra("category_id",category.id)
                startActivity(intent)
            },
            onDelete = { category ->
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                calls = ApiClient.categoryService.deleteProduct("Bearer $token",category.id)
                calls.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@CategoryActivity,"Success Delete Category",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@CategoryActivity,"Failed Delete Category",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        )
        recyclerView.adapter = categoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefreshLayout.setOnRefreshListener { getData() }
        getData()
    }

    private fun getData(){
        swipeRefreshLayout.isRefreshing = true
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        call = ApiClient.categoryService.getAll("Bearer $token")
        call.enqueue(object : Callback<CategoryResponse>{
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if(response.isSuccessful){
                    swipeRefreshLayout.isRefreshing = false
                    val category = response.body()!!.data
                    categoryAdapter.submitList(category)
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}