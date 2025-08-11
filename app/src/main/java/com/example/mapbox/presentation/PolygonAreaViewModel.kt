package com.example.mapbox.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox.data.entity.AreaEntity
import com.example.mapbox.data.relation.PolygonWithArea
import com.example.mapbox.presentation.mapper.toPoints
import com.example.mapbox.presentation.utils.isPolygonClosed
import com.example.mapbox.presentation.utils.nearTo
import com.example.mapbox.service.PolygonAreaService
import com.mapbox.geojson.Point
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PolygonAreaViewModel(
    private val polygonAreaService: PolygonAreaService
) : ViewModel() {

    private val _state = MutableStateFlow(PolygonUiState())
    val state = _state.asStateFlow()

    init {
        observeAreaNames()
        observeUpdatedPolygon()
    }

    fun onSavedAreaClick(areaName: String) {
        safeCallAction(
            action = { polygonAreaService.getPolygonByAreaName(areaName) },
            onSuccess = ::onSaveAreaSuccess,
            onFailure = ::onError
        )
    }

    fun onClearPoints(): Boolean {
        _state.update {
            it.copy(
                polygon = _state.value.polygon
                    .toMutableList()
                    .also { points -> points.clear() }
                    .toList(),
                startZoomAnimation = false
            )
        }
        return true
    }

    fun onAddPoint(point: Point): Boolean {
        _state.update {
            it.copy(
                selectedPoint = point,
                startZoomAnimation = false
            )
        }
        observeUpdatedPolygon() ?: addPointToPolygon(point)
        return true
    }

    fun onPolygonClick(): Boolean {
        _state.update {
            it.copy(
                openAreaDialogue = true,
                startZoomAnimation = false
            )
        }
        return true
    }

    fun onDismissDialogue() {
        _state.update {
            it.copy(
                openAreaDialogue = false,
                startZoomAnimation = false
            )
        }
    }

    fun onAreaNameChange(newAreaName: String) {
        _state.update {
            it.copy(
                areaName = newAreaName,
                startZoomAnimation = false
            )
        }
    }

    fun onSavePolygonClick() {
        safeCallAction(
            action = {
                polygonAreaService.insertPolygon(
                    area = _state.value.toAreaEntity(),
                    polygonPoints = _state.value.toPolygonPointEntities()
                )
            },
            onSuccess = { onSavePolygonSuccess() },
            onFailure = ::onError
        )
    }

    fun onCancelPolygonClick() {
        onDismissDialogue()
        clearDialogueState()
    }

    private fun onSaveAreaSuccess(polygon: List<PolygonWithArea>) {
        _state.update { currentState ->
            currentState.copy(
                polygon = polygon.map { it.points.toPoints() }.last(),
                startZoomAnimation = true
            )
        }
    }

    private fun onSavePolygonSuccess() {
        observeAreaNames()
        onDismissDialogue()
        clearDialogueState()
    }

    private fun addPointToPolygon(point: Point) {
        _state.update {
            it.copy(
                polygon = it.polygon
                    .toMutableList()
                    .also { points -> points.add(point) }
                    .toList(),
                startZoomAnimation = false
            )
        }
    }

    private fun observeAreaNames() {
        polygonAreaService
            .getPolygonAreaNames()
            .onEach(::onAreaNamesSuccess)
            .catch { onError(it) }
            .launchIn(viewModelScope)
    }

    private fun onAreaNamesSuccess(areaNames: List<AreaEntity>) {
        _state.update {
            it.copy(
                savedAreaNames = areaNames.map { area -> area.areaName },
                startZoomAnimation = false
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

    private fun onClearDialogueSuccess() {
        _state.update {
            it.copy(
                areaName = "",
                startZoomAnimation = false
            )
        }
    }

    private fun clearDialogueState() {
        safeCallAction(
            action = { delay(500) },
            onSuccess = { onClearDialogueSuccess() },
            onFailure = { onDismissDialogue() }
        )
    }

    private fun onError(error: Throwable) {
        _state.update {
            it.copy(errorState = error.message)
        }
    }

    private fun <T> safeCallAction(
        action: suspend () -> T,
        onSuccess: (T) -> Unit = {},
        onFailure: (Throwable) -> Unit = {},
    ): Job {
        return viewModelScope.launch {
            runCatching { action() }
                .onSuccess(onSuccess)
                .onFailure(onFailure)
        }
    }
}
