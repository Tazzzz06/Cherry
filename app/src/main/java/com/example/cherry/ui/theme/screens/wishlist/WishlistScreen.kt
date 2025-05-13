package com.example.cherry.ui.theme.screens.wishlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cherry.R

data class Product(val name: String, val price: String, val imageResId: Int)

@Composable
fun WishlistScreen(navController: NavController) {
    val productWishlist = remember {
        mutableStateListOf(
            Product("Stylish Red Dress", "$49.99", R.drawable.dress1),
            Product("Classic Black Shoes", "$39.99", R.drawable.dress1),
            Product("Luxury Leather Bag", "$89.99", R.drawable.dress1)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen title
        Text(
            text = "Your Wishlist",
            fontSize = 32.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sample product image (header)
        Image(
            painter = painterResource(id = R.drawable.dress1),  // Replace with a placeholder or actual dynamic image
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Text to show the added products list
        Text(
            text = "Added Products",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // LazyColumn to display the list of added products
        LazyColumn {
            items(productWishlist) { item ->
                WishlistItem(product = item, onRemove = { productWishlist.remove(item) })
            }
        }
    }
}

@Composable
fun WishlistItem(product: Product, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        Image(
            painter = painterResource(id = product.imageResId),
            contentDescription = product.name,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp)
        )

        // Product Details (Name, Price)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = product.price,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        // Remove Button
        IconButton(onClick = onRemove) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),  // Replace with actual remove icon
                contentDescription = "Remove Product",
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WishlistScreenPreview() {
    WishlistScreen(rememberNavController())
}
