package com.dpadninja.iromium.ui.views

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dpadninja.iromium.helpers.PreferenceUtil
import com.dpadninja.iromium.utils.RGBCorrection
import com.dpadninja.iromium.utils.fromHex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ColorHex", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Color,
    ) {
        val hex =
            "#%02X%02X%02X%02X".format(
                (value.alpha * 255).toInt(),
                (value.red * 255).toInt(),
                (value.green * 255).toInt(),
                (value.blue * 255).toInt(),
            )
        encoder.encodeString(hex)
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color.fromHex(decoder.decodeString()) // ARGB
    }
}

@Serializable
data class Preset(
    @Serializable(with = ColorSerializer::class)
    var color: Color = Color.White,
    @Serializable(with = ColorSerializer::class)
    var labelColor: Color = color,
    var labelIndex: Int = 0,
)

val defaultPresets: MutableList<Preset> =
    mutableListOf(
        Preset(Color(0xFFFF0000)), // Red
        Preset(Color(0xFFFF8000)), // Orange
        Preset(Color(0xFFFFFF00)), // Yellow
        Preset(Color(0xFF00FF00)), // Lime
        Preset(Color(0xFF80FF00)), // Chartreuse
        Preset(Color(0xFF00FFFF)), // Cyan
        Preset(Color(0xFF0000FF)), // Blue
        Preset(Color(0xFF8000FF)), // Violet
        Preset(Color(0xFFFF00FF)), // Fuchsia
        Preset(Color(0xFFFF0080)), // Pink
        Preset(Color(0xFF00FF80)), // Mint
        Preset(Color(0xFF00FFC0)), // Aquamarine
    )

data class UIState(
    val address: String = "",
    val presetIdx: Int = 0,
    val preset: Preset = Preset(),
    val presets: MutableList<Preset> = mutableListOf(),
    val syncWithTVState: Boolean = true,
    val useSmoothTransitions: Boolean = false,
    val rgbCorrection: RGBCorrection = RGBCorrection(),
)

class AppViewModel : ViewModel() {
    var qrCodeText: String = ""
    private val _uiState = MutableStateFlow(UIState())
    private val _testAddress = MutableStateFlow("")
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    val testAddress: StateFlow<String> = _testAddress.asStateFlow()

    init {
        _uiState.value =
            UIState(
                address = AppSettings.address,
                syncWithTVState = AppSettings.syncWithTVState,
                presets = AppSettings.presets,
                presetIdx = AppSettings.presetIdx,
                rgbCorrection = AppSettings.rgbCorrection,
                preset = AppSettings.preset
            )
    }

    fun setTestAddress(value: String) {
        _testAddress.update { value }
    }

    fun setPreset(
        idx: Int,
        preset: Preset,
    ) {
        val presets = _uiState.value.presets
        presets[idx] = preset
        _uiState.update { it.copy(presets = presets) }
        AppSettings.presets = presets
    }

    fun setPresetIdx(idx: Int) {
        val preset = _uiState.value.presets[idx]
        _uiState.update { it.copy(presetIdx = idx, preset = preset) }
        AppSettings.presetIdx = idx
    }

    fun setAddress(value: String) {
        _uiState.update { it.copy(address = value) }
        AppSettings.address = value
    }

    fun setSyncWithTVState(value: Boolean) {
        _uiState.update { it.copy(syncWithTVState = value) }
        AppSettings.syncWithTVState = value
    }

    fun setRGBCorrection(value: RGBCorrection) {
        _uiState.update { it.copy(rgbCorrection = value) }
        AppSettings.rgbCorrection = value
    }
}

object AppSettings : PreferenceUtil() {
    const val SYNC_WITH_TV_STATE = "sync_with_tv_state"
    const val ADDRESS = "address"
    const val PRESET_IDX = "preset_idx"
    const val PRESETS = "presets"
    const val RGB_CORRECTION = "rgb_correction"

    var address: String
        get() = getString(ADDRESS, "")
        set(value) = putString(ADDRESS, value)

    var syncWithTVState: Boolean
        get() = getBoolean(SYNC_WITH_TV_STATE, true)
        set(value) = putBoolean(SYNC_WITH_TV_STATE, value)

    var presetIdx: Int
        get() = getInt(PRESET_IDX, 0)
        set(value) = putInt(PRESET_IDX, value)

    var presets: MutableList<Preset>
        get() {
            val json = getString(PRESETS, Json.encodeToString(defaultPresets))
            return json.let { Json.decodeFromString<MutableList<Preset>>(it) }
        }
        set(value) = putString(PRESETS, Json.encodeToString<MutableList<Preset>>(value))

    var rgbCorrection: RGBCorrection
        get() {
            val json = getString(RGB_CORRECTION, Json.encodeToString(RGBCorrection()))
            return json.let { Json.decodeFromString<RGBCorrection>(it) }
        }
        set(value) = putString(RGB_CORRECTION, Json.encodeToString<RGBCorrection>(value))

    val preset: Preset
        get() = presets[presetIdx]
}
