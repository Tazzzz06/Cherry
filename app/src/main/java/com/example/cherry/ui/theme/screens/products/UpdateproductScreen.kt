package com.example.cherry.ui.theme.screens.products

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cherry.R
import com.example.cherry.data.ProductViewModel
import com.example.cherry.models.ProductModel
import com.example.cherry.navigation.ROUTE_VIEW_PRODUCTS
import com.google.firebase.database.*

@Composable
fun UpdateproductScreen(navController: NavController, productId: String) {
    val context = LocalContext.current
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val existingImageUrl = rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    Log.d("UpdateproductScreen", "Received productId: $productId")
    val currentDataRef = FirebaseDatabase.getInstance().getReference("Products/$productId")
    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val product = snapshot.getValue(ProductModel::class.java)
                    product?.let {
                        name = it.name
                        price = it.price
                        desc = it.desc
                        existingImageUrl.value = it.image
                        Log.d("UpdateproductScreen", "Fetched product: ID=${it.productId}, Image=${it.image}")
                    }
                } else {
                    Toast.makeText(context, "Product not found", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEW_PRODUCTS)
                }
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UpdateproductScreen", "Error fetching product: ${error.message}")
                Toast.makeText(context, "Error fetching product: ${error.message}", Toast.LENGTH_LONG).show()
                isLoading = false
                navController.navigate(ROUTE_VIEW_PRODUCTS)
            }
        }
        currentDataRef.addListenerForSingleValueEvent(listener)
        onDispose { currentDataRef.removeEventListener(listener) }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier.padding(10.dp).size(200.dp)
            ) {
                AsyncImage(
                    model = imageUri.value ?: existingImageUrl.value.ifEmpty { R.drawable.ic_person },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { launcher.launch("image/*") },
                    error = painterResource(R.drawable.ic_person),
                    placeholder = painterResource(R.drawable.ic_person)
                )
            }
            Text(text = "Attach product image")

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Product Name") },
                placeholder = { Text(text = "Please enter product name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(text = "Unit Product Price") },
                placeholder = { Text(text = "Please enter unit product price") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text(text = "Brief description") },
                placeholder = { Text(text = "Please enter product description") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                singleLine = false
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.navigate(ROUTE_VIEW_PRODUCTS) }) {
                    Text(text = "All Products")
                }
                Button(onClick = {
                    val productRepository = ProductViewModel()
                    productRepository.updateProduct(
                        context = context,
                        navController = navController,
                        name = name,
                        price = price,
                        desc = desc,
                        productId = productId,
                        imageUri = imageUri.value,
                        existingImageUrl = existingImageUrl.value
                    )
                }) {
                    Text(text = "UPDATE")
                }
            }
        }
    }
}