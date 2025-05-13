package com.example.cherry.data

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cherry.models.ProductModel
import com.example.cherry.navigation.ROUTE_VIEW_PRODUCTS
import com.example.cherry.network.ImgurService
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ProductViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("Products")
    private val TAG = "ProductViewModel"

    private fun getImgurService(): ImgurService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ImgurService::class.java)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            file.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            file
        } catch (e: Exception) {
            Log.e(TAG, "Failed to process image: ${e.message}", e)
            null
        }
    }

    fun uploadProductWithImage(
        uri: Uri,
        context: Context,
        productname: String,
        productprice: String,
        desc: String,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = getFileFromUri(context, uri)
                if (file == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to process image", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)

                val response = getImgurService().uploadImage(
                    body,
                    "Client-ID 4a9cd0ac9fd5d4f"
                )

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""
                    val productId = database.push().key ?: ""
                    val product = ProductModel(
                        name = productname,
                        price = productprice,
                        desc = desc,
                        productId = productId,
                        image = imageUrl
                    )

                    Log.d(TAG, "Saving product: ID=$productId, Image=$imageUrl")
                    database.child(productId).setValue(product)
                        .addOnSuccessListener {
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Product saved successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ROUTE_VIEW_PRODUCTS)
                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.e(TAG, "Failed to save product: ${e.message}", e)
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Failed to save product: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                } else {
                    Log.e(TAG, "Image upload failed: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Upload error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in uploadProductWithImage: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Exception: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun viewProducts(
        product: MutableState<ProductModel>,
        products: SnapshotStateList<ProductModel>,
        context: Context
    ): SnapshotStateList<ProductModel> {
        val ref = FirebaseDatabase.getInstance().getReference("Products")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                products.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(ProductModel::class.java)
                    val correctProductId = snap.key
                    value?.let {
                        // Temporary fix: Swap productId and image if incorrect
                        val product = if (it.productId.startsWith("https://") && correctProductId != it.productId) {
                            ProductModel(
                                name = it.name,
                                price = it.price,
                                desc = it.desc,
                                productId = correctProductId ?: it.productId,
                                image = it.productId
                            )
                        } else {
                            it
                        }
                        Log.d(TAG, "Fetched product: ID=${product.productId}, Image=${product.image}")
                        products.add(product)
                    }
                }
                if (products.isNotEmpty()) {
                    product.value = products.first()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to fetch products: ${error.message}")
                Toast.makeText(context, "Failed to fetch products: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        return products
    }

    fun updateProduct(
        context: Context,
        navController: NavController,
        name: String,
        price: String,
        desc: String,
        productId: String,
        imageUri: Uri? = null,
        existingImageUrl: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (name.isBlank() || price.isBlank() || desc.isBlank()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                if (productId.isBlank()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Invalid product ID", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                var imageUrl = existingImageUrl
                if (imageUri != null) {
                    val file = getFileFromUri(context, imageUri)
                    if (file != null) {
                        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
                        val response = getImgurService().uploadImage(
                            body,
                            "Client-ID 4a9cd0ac9fd5d4f"
                        )
                        if (response.isSuccessful) {
                            imageUrl = response.body()?.data?.link ?: existingImageUrl
                        } else {
                            Log.e(TAG, "Image upload failed: ${response.code()}")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Failed to upload new image", Toast.LENGTH_SHORT).show()
                            }
                            return@launch
                        }
                    } else {
                        Log.e(TAG, "Failed to process new image")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Failed to process new image", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }
                }

                val databaseReference = FirebaseDatabase.getInstance().getReference("Products/$productId")
                val updatedProduct = ProductModel(
                    name = name,
                    price = price,
                    desc = desc,
                    productId = productId,
                    image = imageUrl
                )

                Log.d(TAG, "Updating product: ID=$productId, Image=$imageUrl")
                databaseReference.setValue(updatedProduct)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Product Updated Successfully", Toast.LENGTH_LONG).show()
                                    navController.navigate(ROUTE_VIEW_PRODUCTS)
                                }
                            }
                        } else {
                            Log.e(TAG, "Product update failed: ${task.exception?.message}")
                            viewModelScope.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Product update failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in updateProduct: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Exception: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteProduct(context: Context, productId: String, navController: NavController) {
        if (productId.isBlank()) {
            Toast.makeText(context, "Invalid product ID", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(context)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("Products/$productId")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Product deleted Successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Log.e(TAG, "Product deletion failed: ${task.exception?.message}")
                        Toast.makeText(context, "Product not deleted: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
fun fixFirebaseProductData() {
    val ref = FirebaseDatabase.getInstance().getReference("Products")
    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (snap in snapshot.children) {
                val product = snap.getValue(ProductModel::class.java)
                val correctProductId = snap.key // The Firebase node key
                if (product != null && product.productId != correctProductId) {
                    // Swap productId and image
                    val updatedProduct = ProductModel(
                        name = product.name,
                        price = product.price,
                        desc = product.desc,
                        productId = correctProductId ?: "",
                        image = product.productId // Use the incorrect productId as the image
                    )
                    snap.ref.setValue(updatedProduct)
                        .addOnSuccessListener {
                            Log.d("ProductViewModel", "Fixed product: $correctProductId")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ProductViewModel", "Failed to fix product $correctProductId: ${e.message}")
                        }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ProductViewModel", "Failed to fix product data: ${error.message}")
        }
    })
}