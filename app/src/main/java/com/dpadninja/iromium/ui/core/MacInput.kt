package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun formatMac(input: String): String {
    // удаляем всё кроме HEX
    val clean = input.uppercase().replace(Regex("[^0-9A-F]"), "")
    return clean.chunked(2).joinToString(":").take(17)
}

@Composable
fun MacInput(
    modifier: Modifier = Modifier,
    address: String = "",
    onAddressFilled: (String) -> Unit,
) {
    var address by remember { mutableStateOf(address) }
    var inputBorderColor by remember { mutableStateOf(textColor) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        UIInternalKeyboardInput(value = address, borderColor = inputBorderColor)
        Spacer(modifier = Modifier.size(6.dp))
        MacKeyboard(
            onPressKey = { address = formatMac(address + it) },
            onPressDone = {
                if (address.length == 17) {
                    inputBorderColor = textColor
                    onAddressFilled(address)
                } else {
                    inputBorderColor = Color(0xCCFF0000)
                }
            },
            onPressDel = { address = address.dropLast(1) },
        )
    }
}
