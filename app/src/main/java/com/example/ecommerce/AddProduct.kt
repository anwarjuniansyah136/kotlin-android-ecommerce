package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.api.ApiClient
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.model.ProductRequest
import com.example.ecommerce.api.util.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddProduct : AppCompatActivity() {
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productQuantity: EditText
    private lateinit var category: EditText
    private lateinit var btnAdd: Button
    private lateinit var sessionManager: SessionManager
    private lateinit var selectedImage: ImageView
    private lateinit var btnSelectImage: Button
    private var imageUri: Uri? = null  // Store the selected image URI
    private var productId: String? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        productName = findViewById(R.id.product_name)
        productPrice = findViewById(R.id.product_price)
        productQuantity = findViewById(R.id.product_quantity)
        category = findViewById(R.id.category)
        btnAdd = findViewById(R.id.btn_add)
        selectedImage = findViewById(R.id.selected_image)
        btnSelectImage = findViewById(R.id.btn_select_image)

        btnSelectImage.setOnClickListener {
            openFileChooser()
        }

        btnAdd.setOnClickListener {
            val product = ProductRequest(
                productName.text.toString(),
                productPrice.text.toString().toInt(),
                productQuantity.text.toString().toInt(),
                category.text.toString()
            )

            ApiClient.init(this)
            sessionManager = SessionManager(this)
            val token = sessionManager.getAuthToken()

            val call = ApiClient.productService.postProduct("Bearer $token", product)
            call.enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if (response.isSuccessful) {
                        productId = response.body()?.data
                        Toast.makeText(this@AddProduct, "Product added successfully", Toast.LENGTH_SHORT).show()

                        imageUri?.let {
                            uploadProductPhoto(productId)
                        }
                    } else {
                        Toast.makeText(this@AddProduct, "Failed to add product", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@AddProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    @SuppressLint("IntentReset")
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            selectedImage.setImageURI(imageUri)
        }
    }

    private fun uploadProductPhoto(productId: String?) {
        if (productId == null || imageUri == null) {
            Toast.makeText(this, "Product ID or image is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val imagePath = getRealPathFromURI(imageUri!!)
        if (imagePath.isEmpty()) {
            Toast.makeText(this, "Failed to retrieve image path", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(imagePath)
        if (!file.exists()) {
            Toast.makeText(this, "Image file does not exist", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val productIdRequest = productId.toRequestBody("text/plain".toMediaTypeOrNull())
//        Log.d("Token", "Bearer ${sessionManager.getAuthToken()}")

//        ApiClient.productService.postPhoto("Bearer ${sessionManager.getAuthToken()}", productIdRequest, photoPart)
//            .enqueue(object : Callback<PostResponse> {
//                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
//                    if (response.isSuccessful) {
//                        Toast.makeText(this@AddProduct, "Photo uploaded successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@AddProduct, "Failed to upload photo", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
//                    Toast.makeText(this@AddProduct, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        val filePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return filePath ?: ""
    }

}

