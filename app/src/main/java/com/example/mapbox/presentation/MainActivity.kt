package com.example.mapbox.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mapbox.maps.MapboxExperimental

class MainActivity : ComponentActivity() {
    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapScreen()
        }
    }
}