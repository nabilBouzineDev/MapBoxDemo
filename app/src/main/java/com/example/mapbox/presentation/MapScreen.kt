package com.example.mapbox.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mapbox.R
import com.example.mapbox.presentation.component.AreaBottomSheet
import com.example.mapbox.presentation.component.AreaNameTextField
import com.example.mapbox.presentation.component.ConfirmationButton
import com.example.mapbox.presentation.ui.currentColor
import com.example.mapbox.presentation.ui.currentTextStyle
import com.example.mapbox.presentation.utils.Constants
import com.example.mapbox.presentation.utils.isPolygonClosed
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.rememberStandardStyleState
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    viewModel: PolygonAreaViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MapBoxContent(
        state = state,
        viewModel = viewModel
    )
}

@Composable
fun MapBoxContent(
    state: PolygonUiState,
    viewModel: PolygonAreaViewModel,
    modifier: Modifier = Modifier
) {

    AreaBottomSheet(
        visible = state.openAreaDialogue,
        onDismissDialogue = viewModel::onDismissDialogue,
        content = {
            AreaNameTextField(
                text = state.areaName,
                onValueChange = viewModel::onAreaNameChange,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .navigationBarsPadding(),

                leadingIcon = R.drawable.rounded_location_on_24
            )
            ConfirmationButton(
                title = "Save",
                onActionClick = viewModel::onSavePolygonClick,
                onCancelClick = viewModel::onCancelPolygonClick,
                isLoading = state.isLoading,
                isEnable = state.areaName.isNotBlank(),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .navigationBarsPadding()
            )
        }
    )


    val mapViewState = rememberMapViewportState {
        setCameraOptions(
            cameraOptions {
                center(Constants.defaultStartingPoint)
                zoom(10.0)
                bearing(0.0)
                pitch(0.0)
                build()
            }
        )
    }

    LaunchedEffect(Unit, state.polygon) {
        if (state.startZoomAnimation) {
            val zoomedPointLong = state.polygon.map { it.longitude() }.average()
            val zoomedPointLat = state.polygon.map { it.latitude() }.average()
            mapViewState.flyTo(
                cameraOptions = cameraOptions {
                    center(Point.fromLngLat(zoomedPointLong, zoomedPointLat))
                    zoom(15.0)
                    bearing(0.0)
                    pitch(0.0)
                    build()
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(800) }
            )
        }
    }
    MapboxMap(
        modifier = modifier
            .fillMaxSize(),
        mapViewportState = mapViewState,
        onMapLongClickListener = { viewModel.onClearPoints() },
        onMapClickListener = viewModel::onAddPoint,
        style = {
            MapboxStandardStyle(
                standardStyleState = rememberStandardStyleState()
            )
        },
        scaleBar = {
            LazyRow(
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .statusBarsPadding()
            ) {
                items(state.savedAreaNames) {
                    FilterChip(
                        selected = true,
                        onClick = { viewModel.onSavedAreaClick(it) },
                        label = {
                            Text(
                                text = it,
                                style = currentTextStyle.normal,
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = currentColor.primary,
                            selectedLabelColor = currentColor.backgroundColor
                        )
                    )
                }
            }
        },
        attribution = {},
        logo = {},
    ) {
        state.polygon.forEach { point ->
            CircleAnnotation(point = point) {
                circleColor = currentColor.primary
                circleStrokeWidth = 2.0
                circleStrokeColor = currentColor.primary
            }
        }

        PolylineAnnotation(points = state.polygon) {
            lineColor = currentColor.primary
            lineWidth = 5.0
            lineOpacity = 0.75
        }

        if (state.polygon.isPolygonClosed()) {
            PolygonAnnotation(points = listOf(state.polygon)) {
                fillColor = currentColor.primary
                fillOpacity = 0.4
                interactionsState.onClicked {
                    viewModel.onPolygonClick()
                }
            }
        }
    }
}