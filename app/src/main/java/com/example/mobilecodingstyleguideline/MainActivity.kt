package com.example.mobilecodingstyleguideline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.mobilecodingstyleguideline.navigation.NavigationHost
import com.example.mobilecodingstyleguideline.ui.theme.CodingStyleGuidelineTheme
import com.tagsamurai.tscomponents.navigation.ModuleScreen
import com.tagsamurai.tscomponents.theme.LocalTheme
import com.tagsamurai.tscomponents.theme.Theme
import com.tagsamurai.tscomponents.utils.LocalActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalActivity provides this@MainActivity,
                LocalTheme provides Theme(module = ModuleScreen.SupplyAsset),

                ) {
                CodingStyleGuidelineTheme {
                    NavigationHost()
                }
            }
        }
    }
}