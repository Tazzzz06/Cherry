package com.example.cherry.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cherry.ui.theme.screens.SplashScreen
import com.example.cherry.ui.theme.screens.dashboard.DashboardScreen
import com.example.cherry.ui.theme.screens.login.LoginScreen
import com.example.cherry.ui.theme.screens.products.AddproductScreen
import com.example.cherry.ui.theme.screens.products.UpdateproductScreen
import com.example.cherry.ui.theme.screens.products.ViewProducts

import com.example.cherry.ui.theme.screens.signup.SignUpScreen
import com.example.shopcart.ui.screens.wishlist.WishlistScreen

@Composable
fun AppNavHost(navController:NavHostController = rememberNavController(),startDestination:String= ROUTE_SPLASH){
    NavHost(navController=navController,startDestination=startDestination){
        composable(ROUTE_SPLASH){ SplashScreen {
            navController.navigate(ROUTE_DASHBOARD) {
                popUpTo(ROUTE_SPLASH){inclusive=true}} }}
        composable(ROUTE_SIGNUP) { SignUpScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }
        composable(ROUTE_DASHBOARD) {
            DashboardScreen(
                navController = navController,
                onGoToWishlist = {
                    navController.navigate(ROUTE_WISHLIST)
                },
                onLogOut = {
                    navController.navigate(ROUTE_LOGIN)
                }
            )
        }
        composable(ROUTE_WISHLIST){ WishlistScreen(navController) }
        composable(ROUTE_ADD_PRODUCTS){ AddproductScreen(navController) }
        composable(ROUTE_VIEW_PRODUCTS){ ViewProducts(navController) }
        composable("$ROUTE_UPDATE_PRODUCT/{productId}") { passedData ->
            val productId = passedData.arguments?.getString("productId")
            if (productId != null) {
                UpdateproductScreen(navController, productId)
            } else {
                // Handle invalid productId (e.g., navigate back or show error)
                navController.popBackStack()
            }
    }

}}