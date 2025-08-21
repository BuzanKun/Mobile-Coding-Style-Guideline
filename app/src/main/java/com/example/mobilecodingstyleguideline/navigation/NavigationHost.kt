package com.example.mobilecodingstyleguideline.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tagsamurai.tscomponents.snackbar.Snackbar
import com.tagsamurai.tscomponents.snackbar.showSnackbar
import com.tagsamurai.tscomponents.theme.theme

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun showSnackBar(message: String, isSuccess: Boolean) {
        snackbarHostState.showSnackbar(scope = scope, message = message, isSuccess = isSuccess)
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = { Snackbar(snackbarHostState = snackbarHostState) },
        containerColor = theme.bodyBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.HomeScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = modifier.padding(innerPadding)
        ) {
            exampleGraph(navController, ::showSnackBar)
        }
    }
}