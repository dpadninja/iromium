package com.dpadninja.iromium.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.dpadninja.iromium.AppContext
import com.dpadninja.iromium.hasBTPermission
import com.dpadninja.iromium.ui.views.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import java.util.UUID

const val BLE_MODULE = "BLE"

suspend fun <T> StateFlow<T>.await(
    expected: T,
    timeoutMs: Long = 5000L,
): Boolean =
    withTimeoutOrNull(timeoutMs) {
        first { it == expected }
    } != null

enum class BleStatus { READY, CONNECTED, CONNECTING, DISCONNECTED }

object BleManager {
    private var _status = MutableStateFlow(BleStatus.DISCONNECTED)
    private var address: String = ""
    val status: StateFlow<BleStatus> = _status
    var services: List<BluetoothGattService>? = null
    var btPermissionGranted: Boolean = false
    var serviceUUID: UUID? = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
    var charUUID: UUID? = UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb")
    var bluetoothGatt: BluetoothGatt? = null
    private var previousPacket: ByteArray? = null
    private var pendingCommand: ByteArray? = null
    private var targetCharacteristic: BluetoothGattCharacteristic? = null

    private val gattCallback =
        object : BluetoothGattCallback() {
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int,
            ) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        Log.d(BLE_MODULE, "Connected")
                        _status.value = BleStatus.CONNECTED
                        gatt.discoverServices()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        Log.w(BLE_MODULE, "Disconnected")
                        _status.value = BleStatus.DISCONNECTED
                        bluetoothGatt?.close()
                        bluetoothGatt = null
                        targetCharacteristic = null
                        retryConnection(gatt.device)
                    }
                }
            }

            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onServicesDiscovered(
                gatt: BluetoothGatt,
                status: Int,
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val char = gatt.getService(serviceUUID)?.getCharacteristic(charUUID)
                    if (char != null) {
                        targetCharacteristic = char
                        _status.value = BleStatus.READY
                        services = gatt.services
                        pendingCommand?.let { writeCommand(it) }
                    } else {
                        Log.e(BLE_MODULE, "No writable characteristic found")
                    }
                } else {
                    Log.e(BLE_MODULE, "Service discovery failed with status $status")
                }
            }
        }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun send(
        command: ByteArray,
    ) {
        pendingCommand = command
        if (command.contentEquals(previousPacket)) return
//        connect()
        if (status.value == BleStatus.READY) {
            Log.d(BLE_MODULE, "Sending: ${command.toHexString()}")
            writeCommand(command)
            previousPacket = command
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(newAddress: String = "") {
        if (status.value == BleStatus.CONNECTING) {
            return
        }
        address = if (newAddress != "") newAddress else AppSettings.address
        if ((!hasBTPermission()) or (address == "")) return
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val device = bluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            Log.e(BLE_MODULE, "Device not found: $address")
            return
        }
        _status.value = BleStatus.CONNECTING
        bluetoothGatt = device.connectGatt(AppContext.context, false, gattCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun retryConnection(device: BluetoothDevice) {
        Handler(Looper.getMainLooper()).postDelayed({
            bluetoothGatt = device.connectGatt(null, false, gattCallback)
        }, 3000)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun writeCommand(command: ByteArray) {
        if (status.value != BleStatus.READY) return
        targetCharacteristic?.value = command
        bluetoothGatt?.writeCharacteristic(targetCharacteristic)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        targetCharacteristic = null
        pendingCommand = null
        _status.value = BleStatus.DISCONNECTED
    }
}
