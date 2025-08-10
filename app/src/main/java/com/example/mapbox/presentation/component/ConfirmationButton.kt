package com.example.mapbox.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mapbox.presentation.ui.currentColor
import com.example.mapbox.presentation.ui.currentTextStyle

@Composable
fun ConfirmationButton(
    title: String,
    onActionClick: () -> Unit,
    onCancelClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(6.dp)

    ) {
        Button(
            onClick = onActionClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnable,
            colors = ButtonDefaults.buttonColors(
                containerColor = currentColor.primary,
                contentColor = currentColor.backgroundColor
            )

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = title,
                    style = currentTextStyle.normal,
                    modifier = Modifier.alignByBaseline()
                )
                AnimatedVisibility(isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 1.dp,
                        color = currentColor.primary,
                        modifier = Modifier
                            .size(12.dp)
                    )
                }
            }
        }
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "cancel",
                style = currentTextStyle.normal,
                color = currentColor.backgroundColor
            )
        }
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationButtonPreview() {
    ConfirmationButton(
        title = "title",
        onActionClick = {},
        onCancelClick = {},
        isLoading = false
    )
}