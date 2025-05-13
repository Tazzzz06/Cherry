package com.example.cherry.ui.theme.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cherry.R
import com.example.cherry.navigation.ROUTE_ADD_PRODUCTS
import com.google.firebase.auth.FirebaseAuth

data class DressItem(val name: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    onGoToWishlist: () -> Unit,
    onLogOut: () -> Unit
) {
    val elegantFont = FontFamily.Serif
    val accentColor = Color(0xFFD4A5A5)

    val dresses = listOf(
        DressItem("Elegant Dress", R.drawable.dress1),
        DressItem("Chic Grad Dress", R.drawable.dress2),
        DressItem("Elegant Lace", R.drawable.dress3),
        DressItem("Lime Green Dress", R.drawable.dress4),
        DressItem("Pink Floral Dress", R.drawable.dress5)
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = { navController.navigate(ROUTE_ADD_PRODUCTS) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_products),
                            contentDescription = "Products",
                            tint = accentColor
                        )
                    }
                    Text("Products", fontSize = 12.sp, color = accentColor)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onGoToWishlist) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wishlist),
                            contentDescription = "Wishlist",
                            tint = accentColor
                        )
                    }
                    Text("Wishlist", fontSize = 12.sp, color = accentColor)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser == null) {
                            navController.navigate("signup")
                        } else {
                            navController.navigate("login")
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_account),
                            contentDescription = "Account",
                            tint = accentColor
                        )
                    }
                    Text("Account", fontSize = 12.sp, color = accentColor)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onLogOut) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "Logout",
                            tint = accentColor
                        )
                    }
                    Text("Logout", fontSize = 12.sp, color = accentColor)
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Dashboard background image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App title
                Text(
                    text = "CHERRY",
                    fontFamily = elegantFont,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(100.dp))

                // Welcome message
                Text(
                    text = "Welcome to Cherry – your destination for elegant, affordable fashion. Explore, wishlist and shop your favorite looks!",
                    fontFamily = elegantFont,
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(60.dp)) // Extra breathing space

                // Middle content placeholder
                Spacer(modifier = Modifier.height(170.dp)) // You can insert a promo banner or CTA here

                // Available Dresses heading
                Text(
                    text = "Available Dresses",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Carousel of dresses
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(dresses) { dress ->
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp)
                                .background(Color.White)
                                .border(1.dp, accentColor, shape = MaterialTheme.shapes.small)
                        ) {
                            Image(
                                painter = painterResource(id = dress.imageRes),
                                contentDescription = dress.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = dress.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        navController = rememberNavController(),
        onGoToWishlist = {},
        onLogOut = {}
    )
}

