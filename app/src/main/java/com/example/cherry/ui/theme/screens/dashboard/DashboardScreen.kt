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
        DressItem("Red Summer Dress", R.drawable.dress1),
        DressItem("Blue Maxi Dress", R.drawable.dress2),
        DressItem("Classic White", R.drawable.dress3),
        DressItem("Floral Elegance", R.drawable.dress4)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CHERRY",
                    fontFamily = elegantFont,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))


                Text(
                    text = "Welcome to Cherry – your destination for elegant, affordable fashion. Explore, wishlist and shop your favorite looks!",
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
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
