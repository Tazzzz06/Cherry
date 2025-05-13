package com.example.cherry.ui.theme.screens.products

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cherry.R
import com.example.cherry.data.ProductViewModel
import com.example.cherry.models.ProductModel
import com.example.cherry.navigation.ROUTE_UPDATE_PRODUCT
import com.example.cherry.navigation.ROUTE_VIEW_PRODUCTS

@Composable
fun ViewProducts(navController: NavHostController) {
    val context = LocalContext.current
    val productRepository = ProductViewModel()
    val emptyUploadState = remember {
        mutableStateOf(ProductModel("", "", "", "", ""))
    }
    val emptyUploadListState = remember {
        mutableStateListOf<ProductModel>()
    }
    val products = productRepository.viewProducts(
        emptyUploadState, emptyUploadListState, context
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Products",
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(products) { product ->
                Log.d("ViewProducts", "Product ID: ${product.productId}, Image URL: ${product.image}")
                ProductItem(
                    name = product.name,
                    price = product.price,
                    desc = product.desc,
                    productId = product.productId,
                    imageUrl = product.image,
                    navController = navController,
                    productRepository = productRepository
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    name: String,
    price: String,
    desc: String,
    productId: String,
    imageUrl: String,
    navController: NavHostController,
    productRepository: ProductViewModel
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .height(210.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.Gray)
        ) {
            Row {
                Column {
                    AsyncImage(
                        model = imageUrl.ifEmpty { R.drawable.ic_person },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                            .padding(10.dp),
                        error = painterResource(R.drawable.ic_person),
                        placeholder = painterResource(R.drawable.ic_person)
                    )

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = {
                                productRepository.deleteProduct(context, productId, navController)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text(
                                text = "REMOVE",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = {
                                Log.d("ProductItem", "Navigating to update_product with productId: $productId")
                                if (productId.isNotBlank() && !productId.startsWith("https://")) {
                                    navController.navigate("$ROUTE_UPDATE_PRODUCT/$productId")
                                } else {
                                    Log.e("ProductItem", "Invalid productId: $productId")
                                    Toast.makeText(context, "Invalid product ID", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            Text(
                                text = "UPDATE",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "PRODUCT NAME",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PRODUCT PRICE",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = price,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "DESC",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = desc,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ViewProductsPreview() {
    ViewProducts(rememberNavController())
}