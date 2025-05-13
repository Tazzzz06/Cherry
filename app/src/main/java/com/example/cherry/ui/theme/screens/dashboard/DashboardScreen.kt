package com.example.cherry.ui.theme.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cherry.R
import com.example.cherry.navigation.ROUTE_ADD_PRODUCTS
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    onGoToWishlist: () -> Unit,
    onLogOut: () -> Unit
) {
    val elegantFont = FontFamily.Serif
    val accentColor = Color(0xFFD4A5A5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CHERRY",
                            fontFamily = elegantFont,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Products
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(ROUTE_ADD_PRODUCTS) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_products),
                            contentDescription = "Products",
                            tint = accentColor
                        )
                    }
                    Text("Products", fontSize = 12.sp, color = accentColor)
                }

                // Wishlist
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onGoToWishlist) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wishlist),
                            contentDescription = "Wishlist",
                            tint = accentColor
                        )
                    }
                    Text("Wishlist", fontSize = 12.sp, color = accentColor)
                }

                // Account
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
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

                // Logout
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                // Add more UI here if needed
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
