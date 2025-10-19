package com.dpadninja.iromium.ui.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun SettingSwitchItem(
    caption: String,
    description: String,
    color: Color = buttonBackgroundColor,
    focusedColor: Color = buttonBackgroundColorFocused,
    switchState: Boolean,
    onClick: (Boolean) -> Unit,
    setFocus: Boolean = false,
) {
    var cardColor by remember { mutableStateOf(color) }
    var isFocusChangeNeeded by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (setFocus and isFocusChangeNeeded) {
            focusRequester.requestFocus()
            isFocusChangeNeeded = false
        }
    }

    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = cardColor,
            ),
        modifier =
            Modifier
                .onFocusChanged {
                    cardColor =
                        if (it.isFocused) {
                            focusedColor
                        } else {
                            color
                        }
                }.padding(bottom = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .focusRequester(focusRequester),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(vertical = 10.dp, horizontal = 14.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false),
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = caption,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = description,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 1.3.em,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = switchState,
                onCheckedChange = {
                    onClick(it)
                },
                thumbContent =
                    if (switchState) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    },
            )
        }
    }
}

@Composable
fun SettingItem(
    caption: String,
    description: String,
    color: Color = buttonBackgroundColor,
    focusedColor: Color = buttonBackgroundColorFocused,
    onClick: () -> Unit,
) {
    var cardColor by remember { mutableStateOf(color) }
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = cardColor,
            ),
        modifier =
            Modifier
                .onFocusChanged {
                    cardColor =
                        if (it.isFocused) {
                            focusedColor
                        } else {
                            color
                        }
                }.padding(bottom = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        onClick = { onClick.invoke() },
    ) {
        Row(
            modifier =
                Modifier
                    .padding(vertical = 10.dp, horizontal = 14.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false),
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = caption,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = description,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 1.3.em,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "",
                modifier = Modifier.size(15.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
