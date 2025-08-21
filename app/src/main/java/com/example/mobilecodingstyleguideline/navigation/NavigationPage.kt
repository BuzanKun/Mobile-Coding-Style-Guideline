package com.example.mobilecodingstyleguideline.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobilecodingstyleguideline.ui.screen.home.view.HomeScreen
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar

fun NavGraphBuilder.exampleGraph(navController: NavHostController, onShowSnackBar: OnShowSnackBar) {

    composable(route = NavigationRoute.HomeScreen.route) {
        HomeScreen(
            onNavigateTo = { route ->
                navController.navigate(route)
            },
            onShowSnackBar
        )
    }

    composable(
        route = NavigationRoute.DetailScreen.route,
        arguments = listOf(
            navArgument(ASSET_ID) {
                type = NavType.StringType
            }
        )
    ) { navBackStackEntry ->
//        val itemId = navBackStackEntry.arguments?.getString(ASSET_ID).orEmpty()
//        DetailScreen(assetId = itemId)
    }
}