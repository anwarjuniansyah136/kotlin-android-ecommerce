package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.SupplyAdapter
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.SupplyProduct
import com.example.ecommerce.api.model.SupplyProductResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplyProductActivity : AppCompatActivity() {
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<SupplyProductResponse>
    private lateinit var supplyAdapter: SupplyAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var calls:Call<LoginResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supply_product)
        swipeRefresh = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)
        supplyAdapter = SupplyAdapter (
            onEditClick = { supplyProduct ->
                val intent = Intent(this,EditSupplyProductActivity::class.java)
                intent.putExtra("supply_id",supplyProduct.id)
                startActivity(intent)
                Log.d("SupplyProductActivity", "Navigating to EditSupplyProductActivity with ID: ${supplyProduct.id}")
            },
            onDeleteClick = { supplyProduct ->
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                calls = ApiClient.supplyService.deleteSupply("Bearer $token",supplyProduct.id)
                calls.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@SupplyProductActivity,"Success To Delete Supply",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        )
        recyclerView.adapter = supplyAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefresh.setOnRefreshListener { getData() }
        getData()
    }

    private fun supplyOnClick(supplyProduct: SupplyProduct) {
        Toast.makeText(applicationContext,supplyProduct.productPrice, Toast.LENGTH_SHORT).show()
    }
    private fun getData(){
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken();
        swipeRefresh.isRefreshing = true
        call = ApiClient.supplyService.getAll("Bearer $token")
        call.enqueue(object : Callback<SupplyProductResponse>{
            override fun onResponse(
                call: Call<SupplyProductResponse>,
                response: Response<SupplyProductResponse>
            ) {
                if(response.isSuccessful){
                    swipeRefresh.isRefreshing = false
                    supplyAdapter.submitList(response.body()!!.data)
                }else{
                    Toast.makeText(this@SupplyProductActivity,"OMG",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SupplyProductResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}