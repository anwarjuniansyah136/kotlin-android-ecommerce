package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.adapter.RelatedProductAdapter
import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductResponse
import com.example.ecommerce.api.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var btnAddCart : Button
    private lateinit var sessionManager: SessionManager
    private lateinit var call:Call<PostResponse>
    private lateinit var btnCheckOut : Button
    private lateinit var calls:Call<PostResponse>
    private lateinit var recyclerView: RecyclerView
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private val BASE_URL = "http://10.0.2.2:8080/product/photos/"
    private lateinit var callss:Call<ProductResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId: String? = intent.getStringExtra("product_id")
        val productName: String? = intent.getStringExtra("product_name")
        val productQuantity: Int = intent.getIntExtra("product_quantity", 0)
        val productPrice: Int = intent.getIntExtra("product_price", 0)
        val productCategory: String? = intent.getStringExtra("product_category")
        val productImage: String? = intent.getStringExtra("product_image")

        findViewById<TextView>(R.id.detail_title).text = productName
        findViewById<TextView>(R.id.detail_quantity).text = "Tersedia : ${ productQuantity } product"
        findViewById<TextView>(R.id.detail_price).text = "Rp ${productPrice}"
        if (productImage != null) {
            val imageUrl = BASE_URL + productImage
            val thumbnail: ImageView = findViewById(R.id.detail_thumbnail)
            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(thumbnail)
        }
        btnAddCart = findViewById(R.id.btn_add_to_cart)
        sessionManager = SessionManager(this)
        ApiClient.init(this)
        val token = sessionManager.getAuthToken()
        btnAddCart.setOnClickListener{
            if (token != null && productId != null) {
                call = ApiClient.cartService.postCart("Bearer $token",productId)
                call.enqueue(object : Callback<PostResponse>{
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(this@ProductDetailActivity,"Success To Add Cart",Toast.LENGTH_SHORT).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this@ProductDetailActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }, 1000)
                        }else{
                            Toast.makeText(this@ProductDetailActivity,"Failed To Add Cart",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                Toast.makeText(this,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }
        }
        btnCheckOut = findViewById(R.id.btn_add_to_checkOut)
        btnCheckOut.setOnClickListener{
            if(token != null && productId != null){
                showQuantityDialog(productId.toString(),productPrice ,token,productQuantity)
            }
        }
        recyclerView = findViewById(R.id.recycler_view)
        relatedProductAdapter = RelatedProductAdapter { product ->  productOnClick(product)}
        recyclerView.adapter = relatedProductAdapter
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@ProductDetailActivity, 2)
        }

        if (token != null && productId != null && productCategory != null) {
                getData(token,productId, productCategory)
        }
    }

    private fun productOnClick(product: Product){
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("product_id", product.id)
        intent.putExtra("product_name", product.productName)
        intent.putExtra("product_quantity", product.productQuantity)
        intent.putExtra("product_price", product.productPrice)
        intent.putExtra("product_category", product.category)
        intent.putExtra("product_image", product.image)
        startActivity(intent)
    }

    private fun getData(token:String,productId:String,categoryId:String){
        ApiClient.init(this)
        callss = ApiClient.productService.findAll("Bearer $token",productId,categoryId)
        callss.enqueue(object :Callback<ProductResponse>{
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful){
                    relatedProductAdapter.submitList(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showQuantityDialog(productId: String, pricePerItem: Int, token: String?,productQuantity: Int) {
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
                performCheckout(productId, quantity, token,productQuantity)
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    private fun performCheckout(productId: String, quantity: Int, token: String,productQuantity:Int) {
        if(quantity > productQuantity){
            Toast.makeText(this,"Stock tidak mencukupi",Toast.LENGTH_SHORT).show()
        }else{
            call = ApiClient.productService.minStock("Bearer $token",productId,quantity)
            call.enqueue(object : Callback<PostResponse>{
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if(response.isSuccessful){
                        calls = ApiClient.checkOutService.postCheckOut("Bearer $token", productId, quantity)
                        calls.enqueue(object : Callback<PostResponse> {
                            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@ProductDetailActivity, "Checkout berhasil!", Toast.LENGTH_SHORT).show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val intent = Intent(this@ProductDetailActivity, HomeActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }, 1000)
                                } else {
                                    Toast.makeText(this@ProductDetailActivity, "Gagal melakukan checkout", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
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
