@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shopcart.ui.screens.wishlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class WishlistProduct(
    val name: String,
    val priceKsh: String,
    val imageRes: Int
)

@Composable
fun WishlistScreen(navController: NavController = rememberNavController()) {
    val wishlistItems = listOf(
        WishlistProduct("Elegant Dress", "Ksh 4,999", R.drawable.dress1),
        WishlistProduct("Chic Grad Dress", "Ksh 8,999", R.drawable.dress2),
        WishlistProduct("Elegant Lace", "Ksh 8,500", R.drawable.dress3),
        WishlistProduct("Lime Green Dress", "Ksh 7,500", R.drawable.dress4),
        WishlistProduct("Pink Floral Dress", "Ksh 4,999", R.drawable.dress5),

        )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Wishlist",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD4A5A5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(wishlistItems) { product ->
                WishlistCard(product)
            }
        }
    }
}

@Composable
fun WishlistCard(product: WishlistProduct) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = product.priceKsh,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Wishlist Icon",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WishlistScreenPreview() {
    WishlistScreen()
}

