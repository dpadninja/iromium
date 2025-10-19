package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MacKeyboard(
    modifier: Modifier = Modifier,
    keys: List<String> = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"),
    onPressKey: (String) -> Unit,
    onPressDel: () -> Unit,
    onPressDone: () -> Unit,
) {
    val allKeys = keys + listOf("DEL", "DONE")
    val buttonSize = 20.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(allKeys.size) { index ->
            val key = allKeys[index]

            when (key) {
                "DEL" ->
                    IconUIButton(
                        text = "Backspace",
                        onClick = { onPressDel() },
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        color = Color(0xFF4D3D3D),
                        focusedColor = Color(0xFFAA3D3F),
                        modifier = Modifier.size(buttonSize),
                    )
                "DONE" ->
                    IconUIButton(
                        text = "DONE",
                        icon = Icons.Filled.Done,
                        onClick = { onPressDone() },
                        color = Color(0xFF3D4D3D),
                        focusedColor = Color(0xFF3DAA4F),
                        modifier = Modifier.size(buttonSize),
                    )
                else ->
                    UIButton(
                        contentPadding = PaddingValues(0.dp),
                        text = key,
                        onClick = { onPressKey(key) },
                        modifier = Modifier.size(buttonSize),
                    )
            }
        }
    }
}
