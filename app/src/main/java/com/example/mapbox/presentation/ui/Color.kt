package com.example.mapbox.presentation.ui

import androidx.compose.ui.graphics.Color

data class MapColor(
    val primary: Color,
    val border: Color,
    val hint: Color,
    val stroke: Color,
    val error: Color,
    val backgroundColor: Color
)

val currentColor = MapColor(
    primary = Color(0xffee4e8b),
    border = Color(0x1F1F1F1F),
    hint = Color(0x611F1F1F),
    stroke = Color(0x991F1F1F),
    error = Color(0xFFE55C5C),
    backgroundColor = Color(0xFF0F0E19)
)