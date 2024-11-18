package com.example.ecommerce.api

import android.content.Context
import com.example.ecommerce.api.service.CartService
import com.example.ecommerce.api.service.CategoryService
import com.example.ecommerce.api.service.CheckOutService
import com.example.ecommerce.api.service.KurirService
import com.example.ecommerce.api.service.LoginService
import com.example.ecommerce.api.service.ProductService
import com.example.ecommerce.api.service.RegisterService
import com.example.ecommerce.api.service.SupplyProduct
import com.example.ecommerce.api.util.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private lateinit var sessionManager: SessionManager

    private val noAuthClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private var authClient: OkHttpClient? = null

    fun init(context: Context) {
        sessionManager = SessionManager.getInstance(context)
        authClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()
    }

    private val noAuthRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(noAuthClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authClient ?: throw IllegalStateException("Auth client not initialized"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val registrasiService: RegisterService by lazy {
        noAuthRetrofit.create(RegisterService::class.java)
    }

    val loginService: LoginService by lazy {
        noAuthRetrofit.create(LoginService::class.java)
    }

    val productService: ProductService by lazy {
        authRetrofit.create(ProductService::class.java)
    }

    val cartService:CartService by lazy {
        authRetrofit.create(CartService::class.java)
    }

    val supplyService:SupplyProduct by lazy {
        authRetrofit.create(SupplyProduct::class.java)
    }

    val categoryService:CategoryService by lazy {
        authRetrofit.create(CategoryService::class.java)
    }

    val kurirService:KurirService by lazy {
        authRetrofit.create(KurirService::class.java)
    }

    val checkOutService:CheckOutService by lazy {
        authRetrofit.create(CheckOutService::class.java)
    }
}

