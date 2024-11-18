package com.example.ecommerce

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.KurirActAdapter
import com.example.ecommerce.api.model.DeliveryResponse
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KurirActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var kurirActAdapter: KurirActAdapter
    private lateinit var call:Call<DeliveryResponse>
    private lateinit var calls:Call<PostResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kurir)
        swipeRefreshLayout = findViewById(R.id.refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)

        kurirActAdapter = KurirActAdapter(
            onClikSuccess = {delivery -> sendToApi(delivery.productId,"Berhasil di Kirim",delivery.sum,delivery.id) },
            onClickBack = {delivery ->  sendToApi(delivery.productId,"Barang di Kembalikan",delivery.sum,delivery.id)}
        )
        recyclerView.adapter = kurirActAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL,false)
        swipeRefreshLayout.setOnRefreshListener { getData() }
        getData()
    }

    private fun sendToApi(productId: String,desc:String,sum:Int,id:String){
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        calls = ApiClient.kurirService.postDel("Bearer $token",productId,desc,sum)
        calls.enqueue(object : Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    calls = ApiClient.kurirService.delete("Bearer $token",id)
                    calls.enqueue(object : Callback<PostResponse>{
                        override fun onResponse(
                            call: Call<PostResponse>,
                            response: Response<PostResponse>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(this@KurirActivity,desc,Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }else {
                    Toast.makeText(this@KurirActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getData(){
        swipeRefreshLayout.isRefreshing = true
        ApiClient.init(this)
        sessionManager = SessionManager(this)
        val token = sessionManager.getAuthToken()
        call = ApiClient.kurirService.getAllDelivery("Bearer $token")
        call.enqueue(object : Callback<DeliveryResponse>{
            override fun onResponse(
                call: Call<DeliveryResponse>,
                response: Response<DeliveryResponse>
            ) {
                if(response.isSuccessful){
                    swipeRefreshLayout.isRefreshing = false
                    kurirActAdapter.submitList(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}