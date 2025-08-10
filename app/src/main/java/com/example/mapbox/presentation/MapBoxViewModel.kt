package com.example.mapbox.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox.presentation.utils.isPolygonClosed
import com.example.mapbox.presentation.utils.nearTo
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapBoxViewModel() : ViewModel() {

    private val _state = MutableStateFlow(MapBoxUiState())
    val state = _state.asStateFlow()

    init {
        observeUpdatedPolygon()
    }

    fun onClearPoints(): Boolean {
        _state.update {
            it.copy(
                polygon = _state.value.polygon.toMutableList().also { it.clear() }.toList()
            )
        }
        return true
    }

    fun onAddPoint(point: Point): Boolean {
        _state.update {
            it.copy(
                selectedPoint = point
            )
        }
        observeUpdatedPolygon() ?: addPointToPolygon(point)
        return true
    }

    fun onPolygonClick(annotation: PolygonAnnotation): Boolean {
        _state.update {
            it.copy(
                openAreaDialogue = true
            )
        }
        return true
    }

    fun onDismissDialogue() {
        _state.update {
            it.copy(
                openAreaDialogue = false
            )
        }
    }

    fun onAreaNameChange(newAreaName: String) {
        _state.update {
            it.copy(
                areaName = newAreaName
            )
        }
    }

    fun onSavePolygonClick() {
        // todo: insert to database
        onDismissDialogue()
        clearDialogueState()
    }

    fun onCancelPolygonClick() {
        onDismissDialogue()
        clearDialogueState()
    }

    private fun addPointToPolygon(point: Point) {
        _state.update {
            it.copy(
                polygon = it.polygon.toMutableList().also { it.add(point) }.toList(),
            )
        }
    }

    private fun observeUpdatedPolygon(): List<Point>? {
        return clearAllPointsIfOutsidePolygon() ?: tryToClosePolygon()
    }

    private fun clearAllPointsIfOutsidePolygon(): List<Point>? {
        return _state.value.polygon
            .takeIf(::onFilterOutsidePolygonOnly)
            ?.also { onClearPoints() }
    }

    private fun tryToClosePolygon(): List<Point>? {
        return _state.value.polygon
            .takeIf(::onFilterFirstPointIsNear)
            ?.also { addPointToPolygon(_state.value.polygon.first()) }
    }

    private fun onFilterOutsidePolygonOnly(points: List<Point>): Boolean {
        return points.isPolygonClosed() && _state.value.selectedPoint !in points
    }

    private fun onFilterFirstPointIsNear(polygon: List<Point>): Boolean {
        return polygon.size > 1 && polygon.first().nearTo(_state.value.selectedPoint)
    }

    fun clearDialogueState() {
        viewModelScope.launch {
            delay(500)
            _state.update {
                it.copy(
                    areaName = "",
                )
            }
        }
    }

}
