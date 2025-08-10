package com.example.mapbox.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mapbox.R
import com.example.mapbox.presentation.component.AreaBottomSheet
import com.example.mapbox.presentation.component.AreaNameTextField
import com.example.mapbox.presentation.component.ConfirmationButton
import com.example.mapbox.presentation.ui.currentColor
import com.example.mapbox.presentation.utils.isPolygonClosed
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
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBoxContent(
    state: MapBoxUiState,
    viewModel: MapBoxViewModel,
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

                leadingIcon = R.drawable.ic_launcher_foreground
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
        onMapLongClickListener = { viewModel.onClearPoints() },
        onMapClickListener = viewModel::onAddPoint,
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
            lineColor = currentColor.primary
            lineWidth = 5.0
        }

        if (state.polygon.isPolygonClosed()) {
            PolygonAnnotation(points = listOf(state.polygon)) {
                fillColor = currentColor.primary
                fillOpacity = 0.4
                interactionsState.onClicked(onClick = viewModel::onPolygonClick)
            }
        }
    }
}