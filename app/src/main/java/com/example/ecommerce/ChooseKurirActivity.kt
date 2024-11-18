package com.example.ecommerce

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.KurirAdapter
import com.example.ecommerce.api.model.DeliveryRequest
import com.example.ecommerce.api.model.KurirResponse
import com.example.ecommerce.api.model.PostResponse

import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseKurirActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<KurirResponse>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var kurirAdapter: KurirAdapter
    private lateinit var calls:Call<PostResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_kurir)
        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)
        val productId = intent.getStringExtra("product_id")
        val sum = intent.getStringExtra("sum")
        val checkOutId = intent.getStringExtra("checkout_id")
        kurirAdapter = KurirAdapter(
            onClick = { kurir ->
                val delivery = DeliveryRequest(productId.toString(),kurir.id,sum.toString().toInt())
                ApiClient.init(this)
                sessionManager = SessionManager(this)
                val token = sessionManager.getAuthToken()
                calls = ApiClient.kurirService.postDelivery("Bearer $token",delivery)
                calls.enqueue(object : Callback<PostResponse>{
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        if(response.isSuccessful){
                            calls = ApiClient.checkOutService.delete("Bearer $token",checkOutId.toString())
                            calls.enqueue(object : Callback<PostResponse>{
                                override fun onResponse(
                                    call: Call<PostResponse>,
                                    response: Response<PostResponse>
                                ) {
                                    if(response.isSuccessful){
                                        Toast.makeText(this@ChooseKurirActivity,"Pesanan Akan Dikirimkan",Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Toast.makeText(this@ChooseKurirActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        )
        recyclerView.adapter = kurirAdapter
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
        call = ApiClient.kurirService.getAll("Bearer $token")
        call.enqueue(object :Callback<KurirResponse>{
            override fun onResponse(call: Call<KurirResponse>, response: Response<KurirResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val listKurir = response.body()
                    if (listKurir != null && listKurir.data.isNotEmpty()) {
                        kurirAdapter.submitList(listKurir.data)
                    } else {
                        Toast.makeText(this@ChooseKurirActivity, "Data kurir kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ChooseKurirActivity, "Gagal memuat data kurir", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<KurirResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}