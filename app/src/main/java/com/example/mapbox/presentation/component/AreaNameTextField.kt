package com.example.mapbox.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapbox.presentation.ui.currentColor
import com.example.mapbox.presentation.ui.currentTextStyle

@Composable
fun AreaNameTextField(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = currentTextStyle.normal,
    hintText: String = "Enter area name",
    maxLines: Int = 1,
    height: Dp = Dp.Unspecified,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    maxCharacters: Int = 24,
    @DrawableRes leadingIcon: Int? = null,
    borderColor: Color = currentColor.border,
    errorColor: Color = currentColor.error,
    primaryColor: Color = currentColor.primary,
    onValueChange: (String) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }

    val currentBorderColor by animateColorAsState(
        targetValue = if (isError) errorColor else if (isFocused) primaryColor else borderColor
    )

    Column {
        Row(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = currentBorderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .defaultMinSize(minHeight = 56.dp)
                .height(height)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (leadingIcon != null) {
                val imageColor by animateColorAsState(
                    targetValue = if (text.isEmpty()) currentColor.hint else currentColor.stroke
                )
                LeadingIcon(leadingIcon, imageColor)
                VerticalDivider(imageColor)
            }
            BasicTextField(
                value = text,
                onValueChange = {
                    if (it.length <= maxCharacters)
                        onValueChange(it)
                    else if (it.length > text.length + 1)
                        onValueChange(it.substring(0, maxCharacters))
                },
                maxLines = maxLines,
                enabled = isEnabled,
                modifier = modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp)
                    .height(height)
                    .clip(RoundedCornerShape(16.dp))
                    .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                textStyle = style.copy(color = currentColor.stroke),
                singleLine = maxLines == 1,
                decorationBox = { innerTextField ->
                    InnerTextFieldWithHint(innerTextField, text, hintText, maxLines, style)
                }
            )
        }
    }
}

@Composable
private fun InnerTextFieldWithHint(
    innerTextField: @Composable (() -> Unit),
    text: String,
    hintText: String,
    maxLines: Int,
    style: TextStyle
) {
    Box(
        modifier = if (maxLines == 1) Modifier else Modifier
            .padding(vertical = 5.dp)
            .padding(top = (if (LocalLayoutDirection.current == LayoutDirection.Rtl) 0 else 3).dp),
        contentAlignment = if (maxLines == 1) Alignment.CenterStart else Alignment.TopStart,
    ) {
        innerTextField()
        if (text.isEmpty()) {
            Text(
                text = hintText,
                style = style,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = currentColor.hint,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun VerticalDivider(
    color: Color
) {
    Box(
        Modifier
            .padding(horizontal = 12.dp, vertical = 13.dp)
            .size(1.dp, 30.dp)
            .background(color)
    )
}

@Composable
private fun LeadingIcon(leadingIcon: Int, imageColor: Color) {
    Image(
        imageVector = ImageVector.vectorResource(id = leadingIcon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(imageColor),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .size(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AreaNameTextFieldPreview() {
    AreaNameTextField(
        text = "HI World"
    )
}