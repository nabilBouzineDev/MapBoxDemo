package com.example.mapbox.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mapbox.R
import com.example.mapbox.presentation.utils.isPolygonClosed
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.rememberStandardStyleState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    viewModel: MapBoxViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MapBoxContent(
        state = state,
        onLongClick = viewModel::onClearPoints,
        onMapClick = viewModel::onAddPoint
    )
}

@Composable
fun MapBoxContent(
    state: MapBoxUiState,
    onLongClick: () -> Boolean,
    onMapClick: (Point) -> Boolean,
    modifier: Modifier = Modifier
) {
    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions(
                cameraOptions {
                    center(state.selectedPoint)
                    zoom(15.0)
                    build()
                }
            )
        },
        onMapLongClickListener = { onLongClick() },
        onMapClickListener = onMapClick,
        style = {
            MapboxStandardStyle(
                standardStyleState = rememberStandardStyleState()
            )
        },
        attribution = {},
        logo = {},
        scaleBar = {}
    ) {
        val markImage = rememberIconImage(
            R.drawable.ic_launcher_foreground,
            painterResource(R.drawable.ic_launcher_foreground)
        )

        state.polygon.forEach { point ->
            PointAnnotation(point = point) {
                iconImage = markImage
            }
        }

        PolylineAnnotation(points = state.polygon) {
            lineColor = Color(0xffee4e8b)
            lineWidth = 5.0
        }

        if (state.polygon.isPolygonClosed()) {
            PolygonAnnotation(points = listOf(state.polygon)) {
                fillColor = Color(0xffee4e8b)
                fillOpacity = 0.4
                interactionsState.onClicked {
                    fillColor = Color(0xff000000)
                    true
                }
            }
        }
    }
}