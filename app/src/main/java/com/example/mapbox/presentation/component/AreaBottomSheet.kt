package com.example.mapbox.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaBottomSheet(
    visible: Boolean,
    onDismissDialogue: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    AnimatedVisibility(visible) {
        ModalBottomSheet(
            modifier = modifier.statusBarsPadding(),
            sheetState = rememberModalBottomSheetState(),
            shape = BottomSheetDefaults.ExpandedShape,
            onDismissRequest = onDismissDialogue,
            content = content,
            contentWindowInsets = {
                BottomSheetDefaults.windowInsets.exclude(WindowInsets.navigationBars)
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AreaBottomSheetPreview() {
    AreaBottomSheet(
        visible = true,
        onDismissDialogue = {}
    )
}