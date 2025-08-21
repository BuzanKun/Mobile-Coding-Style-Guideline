package com.example.mobilecodingstyleguideline.navigation

const val ASSET_ID = "assetId"

sealed class NavigationRoute(val route: String) {
    data object HomeScreen : NavigationRoute(route = "home_screen")

    data object DetailScreen : NavigationRoute(route = "detail_screen/{$ASSET_ID}") {
        fun navigate(assetId: String) = route.replace("{$ASSET_ID}", assetId)
    }
}